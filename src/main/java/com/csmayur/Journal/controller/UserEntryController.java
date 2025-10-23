package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.repository.UserEntryRepo;
import com.csmayur.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserEntryController {

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserEntity> getAllUser() {
        return userService.getAll();
    }


    @Autowired
    public UserEntryRepo userEntryRepo;


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserEntity userEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        UserEntity userInDb = userService.findByUserName(userName);
            userInDb.setUserName(userEntity.getUserName());
            userInDb.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userService.saveEntry(userInDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserEntity userInDb = userService.findByUserName(userName);
        userService.deleteById(userInDb.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

