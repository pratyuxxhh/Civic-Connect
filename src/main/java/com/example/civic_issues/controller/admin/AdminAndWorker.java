package com.example.civic_issues.controller.admin;

import com.example.civic_issues.controller.worker.Worker;
import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.WorkerPOJO;
import com.example.civic_issues.repository.adminRepo.AdminRepository;
import com.example.civic_issues.repository.workerRepo.WorkerRepository;
import com.example.civic_issues.services.issueService.IssueService;
import com.example.civic_issues.services.workerService.WorkerServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
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
    private WorkerRepository workerRepository;
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
    // get all un-hired worker
    @GetMapping("/get-un-hired-workers/{dist}")
    public ResponseEntity<?>getUnHiredWorker(@PathVariable Double dist){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO admin =adminRepository.findByUserName(auth.getName());
        Distance distance = new Distance(dist, Metrics.KILOMETERS);
        List<WorkerPOJO> workers = workerRepository.findByDepartmentAndIsHiredAndLocationNear(admin.getDepartment(),false,admin.getLocation(),distance);
        if(workers!=null){
            return  new ResponseEntity<>(workers,HttpStatus.FOUND);
        }
        else return  new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }

    // add new worker
    @PutMapping("/add-this-worker/{id}")
    public ResponseEntity<?> addThisUser(@PathVariable WorkerPOJO worker){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO admin = adminRepository.findByUserName(auth.getName());
        WorkerPOJO workerPOJO = workerRepository.findByUserName(worker.getUserName());
        if(workerPOJO!=null){
            workerPOJO.setIsHired(true);
            admin.getWorkers().add(workerPOJO);
            workerRepository.save(workerPOJO);
            adminRepository.save(admin);
            return new ResponseEntity<>("new worker hired" , HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>("no worker found " , HttpStatus.NOT_FOUND);
        }
    }


    //remove worker
    @PutMapping("/remove-worker/{username}")
    public ResponseEntity<String> removeThisWorker(@PathVariable String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO admin = adminRepository.findByUserName(auth.getName());

        return workerServices.removeThisWorker(username , admin);
    }

    //change department of issue
    @PutMapping("/change-issue-dept/{id}/{changedDept}")
    public  ResponseEntity<String> changeDeptOfThisIssue(@PathVariable ObjectId id , @PathVariable String changedDept){
        return issueService.changeTheDept(id , changedDept);
    }

    //double-check the validity of the issue

    //assign worker algo
    // a list of all the workers near 3 km radius will get the notification of the issue
    // the first who accept will get assigned task and has to do the work

    //remove a worker
    @PutMapping("/fire-this-worker/{name}")
    public ResponseEntity<String> fireThisWorker(@PathVariable String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AdminPOJO admin =adminRepository.findByUserName(auth.getName());
        WorkerPOJO worker = workerRepository.findByUserName(username);
        worker.setIsHired(false);
        worker.setIsBusy(false);
        workerRepository.save(worker);
        admin.getWorkers().remove(worker);
        adminRepository.save(admin);
        return new ResponseEntity<>("worker Removed" , HttpStatus.OK);

    }


}
