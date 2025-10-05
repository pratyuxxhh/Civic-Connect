package com.example.civic_issues.controller.admin;


import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.services.adminService.AdminServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    //delete admin
    @DeleteMapping("/delete")
    public ResponseEntity<String > deleteAdmin(){

        try {
            adminServices.deleteAdmin();
            return new ResponseEntity<>("admin delete successfully ", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //set profile
    @PutMapping("/my-profile")
    public ResponseEntity<String> updateProfile(@RequestBody AdminPOJO pojo){
        adminServices.updateMyProfile(pojo);
        return new ResponseEntity<>("profile updated successfully " ,HttpStatus.ACCEPTED);
    }

}
