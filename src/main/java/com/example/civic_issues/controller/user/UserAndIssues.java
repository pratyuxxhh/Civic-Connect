package com.example.civic_issues.controller.user;

import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.IssuePOJO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.repository.issueRepo.IssueRepository;
import com.example.civic_issues.repository.userRepo.UserRepository;
import com.example.civic_issues.services.aiService.AiService;
import com.example.civic_issues.services.issueService.IssueService;
import com.example.civic_issues.services.userService.UserServices;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")  //yaha tk to user signIn ho k hi aaega
public class UserAndIssues {
    @Autowired
    private UserServices userServices;
    @Autowired
    private IssueService issueService;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AiService aiService;

    @GetMapping("/health-check")
    public ResponseEntity<String> checkHealth(){
        return ResponseEntity.ok("hey , UserAndIssues is fine ");
    }

    @PostMapping("/upload-new-issue")
    public ResponseEntity<String> uploadAnIssue(@RequestPart("lat") String latitude,@RequestPart("long") String longitude,@RequestPart("dept") String department,@RequestPart("desc") String description, @RequestPart("fileData") MultipartFile fileData){
        try{


            //validate by AI
            String base64Image = Base64.getEncoder().encodeToString(fileData.getBytes());
            boolean isValid = aiService.validate(base64Image,description,department);
            //search for a better prompt
            if (isValid) {
                userServices.uploadIssues(latitude, longitude, department, description, fileData);
                return new ResponseEntity<>("issue Uploaded", HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("its not a valid image",HttpStatus.ACCEPTED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("some error occurred", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/issue/{id}")
    public ResponseEntity<String> deleteAnIssue(@PathVariable ObjectId id){
            return issueService.deleteThisIssue(id);
    }

    @PutMapping("/set-location")
    public ResponseEntity<String> setThisLocation(@RequestParam double lat,@RequestParam Double lon){
        return userServices.setThisLocation(lon , lat);
    }

    @GetMapping("/my-issues")
    public ResponseEntity<List<IssuePOJO>> getMyIssues(){
        List<IssuePOJO> myIssues = userServices.getMyIssues();
        return  new ResponseEntity<>(myIssues, HttpStatus.FOUND);
    }

    @GetMapping("/issue/{id}")
    public ResponseEntity<Map<String, String>> getIssueDetails(@PathVariable String id) {
        IssuePOJO issue = issueRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        String base64Image = Base64.getEncoder().encodeToString(issue.getImageData());

        Map<String, String> response = new HashMap<>();
        response.put("description", issue.getDescription());
        response.put("image", base64Image);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/my-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserPOJO pojo){
        userServices.updateMyProfile(pojo);
        return new ResponseEntity<>("profile updated successfully " ,HttpStatus.ACCEPTED);
    }

    //it is complex --done
    @GetMapping("/get-nearby-issues/{distance}")
    public ResponseEntity<List<IssuePOJO>> getNearByIssues(@PathVariable Double dist) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPOJO currentUser = userRepository.findByUserName(auth.getName());
        return issueService.getNearByIssues(currentUser , dist);
    }

    //voting system

}
