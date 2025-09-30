package com.example.civic_issues.services.implementation;

import com.example.civic_issues.entities.SigninRequestDTO;
import com.example.civic_issues.repository.AuthRepo;
import com.example.civic_issues.repository.adminRepo.AdminRepository;
import com.example.civic_issues.repository.userRepo.UserRepository;
import com.example.civic_issues.repository.workerRepo.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private AuthRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SigninRequestDTO  auth = repo.findByUserName(username);

        return User.builder()
                .username(auth.getUserName())
                .password(auth.getPassword())
                .roles(auth.getRole())
                .build();
    }

}
