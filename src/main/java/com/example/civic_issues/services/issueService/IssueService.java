package com.example.civic_issues.services.issueService;

import com.example.civic_issues.entities.IssuePOJO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.repository.issueRepo.IssueRepository;
import com.example.civic_issues.repository.userRepo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;

    public void saveThisIssue(IssuePOJO issue) {
        issueRepository.save(issue);
    }

    public ResponseEntity<String> deleteThisIssue(ObjectId id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserPOJO user =userRepository.findByUserName(auth.getName()) ;
            IssuePOJO issue = issueRepository.findById(id).orElse(null);
            if (issue != null) {
                issueRepository.delete(issue);
                user.getIssues().remove(issue);
                userRepository.save(user);
                return new ResponseEntity<>("issue deleted successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("no such issue found " , HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<List<IssuePOJO>> getNearByIssues(UserPOJO currentUser, Double dist) {
        try {
            GeoJsonPoint userLocation = currentUser.getLocation();
            Distance distance = new Distance(dist, Metrics.KILOMETERS);
            List<IssuePOJO> issues = issueRepository.findByLocationNear(currentUser.getLocation(),distance);
            return  new ResponseEntity<>(issues , HttpStatus.FOUND);
        }
        catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<String> changeTheDept(ObjectId id, String changedDept) {
        IssuePOJO issue = issueRepository.findById(id).orElse(null);
        if(issue!=null){
            issue.setDepartment(changedDept.toUpperCase());
            issueRepository.save(issue);
            return  new ResponseEntity<>("department changed successfully ",HttpStatus.ACCEPTED);
        }
        return  new ResponseEntity<>("no issue found" , HttpStatus.NOT_FOUND);
    }
}
