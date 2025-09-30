package com.example.civic_issues.services.issueService;

import com.example.civic_issues.entities.IssuePOJO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.repository.issueRepo.IssueRepository;
import com.example.civic_issues.repository.userRepo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
}
