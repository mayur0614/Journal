package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserEntryController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserEntity> getAllUser(){
        return userService.getAll();
    }
    @PostMapping
    public void createUser(@RequestBody UserEntity userEntity){
        userService.saveEntry(userEntity);
    }
    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity userEntity,@PathVariable String userName){
        UserEntity userInDb =  userService.findByUserId(userName) ;
        if(userInDb!= null){
            userInDb.setUserName(userEntity.getUserName());
            userInDb.setPassword(userEntity.getPassword());
            userService.saveEntry(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
