package com.example.civic_issues.controller.admin;

import com.example.civic_issues.controller.worker.Worker;
import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.WorkerPOJO;
import com.example.civic_issues.repository.adminRepo.AdminRepository;
import com.example.civic_issues.services.issueService.IssueService;
import com.example.civic_issues.services.workerService.WorkerServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminAndWorker {
    @Autowired
    private WorkerServices workerServices;
    @Autowired
    private IssueService issueService;
    @Autowired
    private AdminRepository adminRepository;
    //workers list
    @GetMapping("/my-workers")
    public ResponseEntity<List<WorkerPOJO>> getMyWorkers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO admin =adminRepository.findByUserName(auth.getName());
        List<WorkerPOJO> myWorkers = workerServices.getWorkersByDepartment(admin.getDepartment());
        return  new ResponseEntity<>(myWorkers , HttpStatus.FOUND);
    }

    // add new worker

    //remove worker
    @PutMapping("/remove-worker/{id}")
    public ResponseEntity<String> removeThisWorker(@PathVariable ObjectId id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO admin = adminRepository.findByUserName(auth.getName());
        return workerServices.removeThisWorker(id , admin);
    }

    //change department of issue
    @PutMapping("/change-issue-dept/{id}/{changedDept}")
    public  ResponseEntity<String> changeDeptOfThisIssue(@PathVariable ObjectId id , @PathVariable String changedDept){
        return issueService.changeTheDept(id , changedDept);
    }

    //double-check the validity of the issue

    //assign worker algo

}
