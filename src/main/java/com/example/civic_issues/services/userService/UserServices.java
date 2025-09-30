package com.example.civic_issues.services.userService;

import com.example.civic_issues.entities.IssuePOJO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.entities.SigninRequestDTO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.repository.AuthRepo;
import com.example.civic_issues.repository.issueRepo.IssueRepository;
import com.example.civic_issues.repository.userRepo.UserRepository;
import com.example.civic_issues.services.issueService.IssueService;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthRepo auth;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IssueService issueService;

    public boolean saveUser(SigninRequestDTO dto) {
        if(auth.existsByUserName(dto.getUserName())) return false;

        UserPOJO user= new UserPOJO();
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");
        dto.setRole(dto.getRole().toUpperCase());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        auth.save(dto);
        userRepository.save(user);
        return  true;
    }

    public void uploadIssues( String latitude, String longitude, String department, String description, MultipartFile filedata) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserPOJO user = userRepository.findByUserName(username);
        double lat=Double.parseDouble(latitude);
        double lon=Double.parseDouble(longitude);
        IssuePOJO issue = new IssuePOJO();
        byte[] data = filedata.getBytes();
        issue.setLocation(new GeoJsonPoint(lon, lat));
        issue.setDepartment(department);
        issue.setDescription(description);
        issue.setDate(LocalDate.now());
        issue.setStatus("PENDING");
        issue.setImageData(data);

        issueService.saveThisIssue(issue);
        user.getIssues().add(issue);
        userRepository.save(user);


    }

    public List<IssuePOJO> getMyIssues()  {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=  auth.getName();
        try{
            UserPOJO user = userRepository.findByUserName(username);
            return user.getIssues();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateMyProfile(UserPOJO pojo) {
        UserPOJO user = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            if(pojo.getFullName()!=null) user.setFullName(pojo.getFullName());
            if(pojo.getAddress()!=null) user.setAddress(pojo.getAddress());
            if(pojo.getEmail()!=null) user.setEmail(pojo.getEmail());
            userRepository.save(user);
    }

    public ResponseEntity<String> setThisLocation(Double lon, double lat) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserPOJO user = userRepository.findByUserName(auth.getName());
            user.setLocation(new GeoJsonPoint(lon,lat));
            userRepository.save(user);
            return new ResponseEntity<>("location updated", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

