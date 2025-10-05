package com.example.civic_issues.services.workerService;

import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.WorkerPOJO;
import com.example.civic_issues.entities.SigninRequestDTO;
import com.example.civic_issues.repository.AuthRepo;
import com.example.civic_issues.repository.adminRepo.AdminRepository;
import com.example.civic_issues.repository.workerRepo.WorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WorkerServices {
    @Autowired
    private AuthRepo auth;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean saveWorker(SigninRequestDTO dto) {
        if(auth.existsByUserName(dto.getUserName())) return false;
        WorkerPOJO worker= new WorkerPOJO();
        worker.setUserName(dto.getUserName());
        worker.setPassword(passwordEncoder.encode(dto.getPassword()));
        worker.setRole("WORKER");
        worker.setIsHired(false);
        dto.setRole(dto.getRole().toUpperCase());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        auth.save(dto);
        workerRepository.save(worker);
        return true;
    }

    public ResponseEntity<String> setThisLocation(Double latitude, Double longitude) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        WorkerPOJO worker = workerRepository.findByUserName(username);
        try {
            if (latitude != null && longitude != null) {
                worker.setLocation(new GeoJsonPoint(longitude ,latitude));

                workerRepository.save(worker);
            }
            return new ResponseEntity<>("location updated", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error("some error occurred : " + e);
            return  new ResponseEntity<>("some error occurred ", HttpStatusCode.valueOf(500));
        }
    }
    public List<WorkerPOJO> getWorkersByDepartment(String department) {
        return workerRepository.findByDepartment(department);
    }

    public List<WorkerPOJO> getHiredWorkers(String department) {
        return workerRepository.findByDepartmentAndIsHired(department, true);
    }

    public List<WorkerPOJO> getBusyWorkers(String department) {
        return workerRepository.findByDepartmentAndIsBusy(department, true);
    }

    public List<WorkerPOJO> getAvailableWorkers(String department) {
        return workerRepository.findByDepartmentAndIsHiredAndIsBusy(department, true, false);
    }
    public void updateMyProfile(WorkerPOJO pojo) {
        WorkerPOJO worker = workerRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        if(pojo.getFullName()!=null) worker.setFullName(pojo.getFullName());
        if(pojo.getAddress()!=null) worker.setAddress(pojo.getAddress());
        if(pojo.getEmail()!=null) worker.setEmail(pojo.getEmail());
        if(pojo.getDepartment()!=null) worker.setDepartment(pojo.getDepartment());
        workerRepository.save(worker);
    }

    public ResponseEntity<String> removeThisWorker(String username, AdminPOJO admin) {
        WorkerPOJO worker = workerRepository.findByUserName(username);
        if(worker!=null){
            admin.getWorkers().remove(worker);
            worker.setIsHired(Boolean.FALSE);
            adminRepository.save(admin);
            return new ResponseEntity<>("worker removed successfully " ,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("no worker removed" ,HttpStatus.BAD_REQUEST);
    }
}
