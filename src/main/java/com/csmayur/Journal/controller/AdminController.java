package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.JournalEntry;
import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getALlUsers() {
        List<UserEntity> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public  String createAdmin(@RequestBody UserEntity userEntity){
        userService.saveAdmin(userEntity);
        return "created";
    }
}
