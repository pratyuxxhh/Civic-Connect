package com.example.civic_issues.controller.worker;


import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.entities.WorkerPOJO;
import com.example.civic_issues.entities.IssuePOJO;
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

    //get incomplete tasks


}
