package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserEntity> getAllUser() {
        return userService.getAll();
    }
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        // Encode password using injected bean
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(Arrays.asList("USER"));
        UserEntity savedUser = userService.saveEntry(userEntity);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    /*
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        if (userEntity.getRoles() == null || userEntity.getRoles().isEmpty()) {
            userEntity.setRoles(Arrays.asList("USER"));
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUser = userService.saveEntry(userEntity);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }*/

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity userEntity, @PathVariable String userName) {
        UserEntity userInDb = userService.findByUserId(userName);
        if (userInDb != null) {
            userInDb.setUserName(userEntity.getUserName());
            userInDb.setPassword(userEntity.getPassword());
            userService.saveEntry(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<?> deleteUser(@PathVariable String userName){
        UserEntity userInDb = userService.findByUserId(userName);
        if(userInDb != null){
            userService.deleteById(userInDb.getId());
            return new ResponseEntity<>(userInDb , HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Username not found",HttpStatus.NOT_FOUND);
        }
    }
}

