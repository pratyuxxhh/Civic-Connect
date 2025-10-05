package com.example.civic_issues.controller.worker;


import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.entities.WorkerPOJO;
import com.example.civic_issues.entities.IssuePOJO;
import com.example.civic_issues.repository.issueRepo.IssueRepository;
import com.example.civic_issues.repository.workerRepo.WorkerRepository;
import com.example.civic_issues.services.workerService.WorkerServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/worker")   // yaha tk to user signin ho k hi aaaega
public class Worker {
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private WorkerServices workerServices;

    @GetMapping("/health-check")
    public ResponseEntity<String> checkHealth(){
        return ResponseEntity.ok("hey , the worker is fine ");
    }

    @GetMapping("/get-assigned-tasks")
    public ResponseEntity<List<IssuePOJO>> getAllTasks(){

        return null;
    }

    @PutMapping("/my-profile")
    public ResponseEntity<String> updateProfile(@RequestBody WorkerPOJO pojo){
        workerServices.updateMyProfile(pojo);
        return new ResponseEntity<>("profile updated successfully " ,HttpStatus.ACCEPTED);
    }
    @PutMapping("/set-location")
    public ResponseEntity<String> setLocation(@RequestParam Double latitude, @RequestParam Double longitude)
    {
        return workerServices.setThisLocation(latitude,longitude);
    }

    //get completed tasks
    @GetMapping("/get-complete-tasks")
    public ResponseEntity<List<IssuePOJO>> getCompleteTasks(){
        List<IssuePOJO>issues =issueRepository.findByStatus("COMPLETED");
        if(issues!=null){
            return new ResponseEntity<>(issues,HttpStatus.ACCEPTED);
        }
        return  new ResponseEntity<>(null ,HttpStatus.NOT_FOUND);

    }

    //get incomplete tasks
    @GetMapping("/get-incomplete-tasks")
    public ResponseEntity<List<IssuePOJO>> getIncompleteTasks(){
        List<IssuePOJO>issues =issueRepository.findByStatus("INCOMPLETE");
        if(issues!=null){
            return new ResponseEntity<>(issues,HttpStatus.ACCEPTED);
        }
        return  new ResponseEntity<>(null ,HttpStatus.NOT_FOUND);

    }
    //get assigned tasks
    @GetMapping("/get-assigned-tasks")
    public ResponseEntity<List<IssuePOJO>> getAssignedTasks(){
        List<IssuePOJO>issues =issueRepository.findByStatus("ASSIGNED");
        if(issues!=null){
            return new ResponseEntity<>(issues,HttpStatus.ACCEPTED);
        }
        return  new ResponseEntity<>(null ,HttpStatus.NOT_FOUND);

    }
    //get inprogress tasks
    @GetMapping("/get-inprogress-tasks")
    public ResponseEntity<List<IssuePOJO>> getInprogressTasks(){
        List<IssuePOJO>issues =issueRepository.findByStatus("INPROGRESS");
        if(issues!=null){
            return new ResponseEntity<>(issues,HttpStatus.ACCEPTED);
        }
        return  new ResponseEntity<>(null ,HttpStatus.NOT_FOUND);

    }

}
