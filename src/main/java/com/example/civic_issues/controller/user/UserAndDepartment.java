package com.example.civic_issues.controller.user;

import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.UserPOJO;
import com.example.civic_issues.repository.userRepo.UserRepository;
import com.example.civic_issues.services.adminService.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserAndDepartment {
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/nearby-admins/{distance}")
    public ResponseEntity<List<AdminPOJO>> getNearByAdmins(@PathVariable Double distance){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPOJO currentUser = userRepository.findByUserName(auth.getName());
        List<AdminPOJO> admins = adminServices.getAdminsNearBy( currentUser,distance);
        return new ResponseEntity<>(admins , HttpStatus.FOUND);

    }
}
