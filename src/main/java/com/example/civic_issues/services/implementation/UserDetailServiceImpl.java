package com.example.civic_issues.services.implementation;

import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.SigninRequestDTO;
import com.example.civic_issues.repository.AuthRepo;
import com.example.civic_issues.repository.adminRepo.AdminRepository;
import com.example.civic_issues.repository.userRepo.UserRepository;
import com.example.civic_issues.repository.workerRepo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private AuthRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SigninRequestDTO  auth = repo.findByUserName(username);
        if(auth.getRole().equals("ADMIN")){

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            authorities.add(new SimpleGrantedAuthority(auth.getDepartment().toUpperCase()));

            return new User(auth.getUserName(), auth.getPassword(), authorities);
        }
        return User.builder()
                .username(auth.getUserName())
                .password(auth.getPassword())
                .roles(auth.getRole())
                .build();
    }

}
