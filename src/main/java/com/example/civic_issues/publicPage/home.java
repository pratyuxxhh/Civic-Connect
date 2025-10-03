package com.example.civic_issues.publicPage;

import com.example.civic_issues.controller.admin.Admin;
import com.example.civic_issues.entities.SigninRequestDTO;
import com.example.civic_issues.services.adminService.AdminServices;
import com.example.civic_issues.services.userService.UserServices;
import com.example.civic_issues.services.workerService.WorkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class home {
    @Autowired
    private UserServices userServices;
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private WorkerServices workerServices;

    @PostMapping("/signup")
    public ResponseEntity<SigninRequestDTO> signUpUser(@RequestBody SigninRequestDTO dto) {
        String role = dto.getRole().toUpperCase();
        switch (role) {
            case "USER":
                if(userServices.saveUser(dto)){
                    return new ResponseEntity<>(dto, HttpStatus.CREATED);
                }
                break;
            case "WORKER":
                if(workerServices.saveWorker(dto)){
                    return new ResponseEntity<>(dto, HttpStatus.CREATED);
                }
            case "ADMIN":
                if(adminServices.saveAdmin(dto)){
                    return new ResponseEntity<>(dto, HttpStatus.CREATED);
                }
            default:
                return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(){
        return new ResponseEntity<>("everything is fine",HttpStatus.OK);
    }
}
