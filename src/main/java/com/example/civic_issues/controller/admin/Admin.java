package com.example.civic_issues.controller.admin;


import com.example.civic_issues.services.adminService.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class Admin {
    @Autowired
    private AdminServices adminServices;

    @GetMapping("/health-check")
    public ResponseEntity<String> checkHealth(){
        return ResponseEntity.ok("heyy ,The Admin is Okay" );
    }

    //department location  -- done
    @PutMapping("/set-location")
    public ResponseEntity<String> setThisLocation(@RequestParam double lat,@RequestParam Double lon){
        return adminServices.setThisLocation(lon , lat);
    }

    //workers list
    //workers location
    // add new worker
    //remove worker
    //change department of issue
    //double-check the validity of the issue

}
