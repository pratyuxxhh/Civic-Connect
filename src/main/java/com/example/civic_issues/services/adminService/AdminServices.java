package com.example.civic_issues.services.adminService;

import com.example.civic_issues.controller.admin.Admin;
import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.SigninRequestDTO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.repository.AuthRepo;
import com.example.civic_issues.repository.adminRepo.AdminRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServices {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AuthRepo auth;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public boolean saveAdmin(SigninRequestDTO dto) {
        if(auth.existsByUserName(dto.getUserName()))return false;
        AdminPOJO admin= new AdminPOJO();
        admin.setUserName(dto.getUserName());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setRole("ADMIN");
        dto.setRole(dto.getRole().toUpperCase());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        auth.save(dto);
        adminRepository.save(admin);
        return true;
    }

    public List<AdminPOJO> getAdminsNearBy(UserPOJO currentUser, Double distanceKM) {
        GeoJsonPoint userLocation = currentUser.getLocation();
        Distance distance = new Distance(distanceKM, Metrics.KILOMETERS);

        return adminRepository.findByLocationNear(userLocation, distance);
    }

    public ResponseEntity<String> setThisLocation(Double lon, double lat) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AdminPOJO admin = adminRepository.findByUserName(auth.getName());
            admin.setLocation(new GeoJsonPoint(lon,lat));
            adminRepository.save(admin);
            return new ResponseEntity<>("location updated", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO admin = adminRepository.findByUserName(auth.getName());
        adminRepository.delete(admin);
    }

    public void updateMyProfile(AdminPOJO pojo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO user = adminRepository.findByUserName(auth.getName());
        if(pojo.getFullName()!=null) user.setFullName(pojo.getFullName());
        if(pojo.getAddress()!=null) user.setAddress(pojo.getAddress());
        if(pojo.getEmail()!=null) user.setEmail(pojo.getEmail());
        adminRepository.save(user);
    }


}
