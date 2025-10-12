package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK kk";
    }

    @PostMapping
    public void createUser(@RequestBody UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(Arrays.asList("USER"));
        userService.saveEntry(userEntity);
    }
}
