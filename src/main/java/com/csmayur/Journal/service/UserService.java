package com.csmayur.Journal.service;

import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.repository.UserEntryRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserEntryRepo userEntryRepo;


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public Boolean saveEntry(UserEntity userEntity){
        try{
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRoles(Arrays.asList("USER"));
            userEntryRepo.save(userEntity);
            log.info("User saved successfully");
            log.debug("from debug successful");
            return true ;
        } catch(Exception e){
            log.debug("from debug failed");
            log.error("Error saving user", e);
            return false;
        }
    }

    /*public Boolean saveEntry(UserEntity userEntity){
        try{
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRoles(Arrays.asList("USER"));
            userEntryRepo.save(userEntity);
            return true ;
        }catch(Exception e){
            logger.info("hahahah");
            return false;
        }
    }*/
    public UserEntity saveAdmin(UserEntity userEntity){
        userEntity.setRoles(Arrays.asList("USER","ADMIN"));
        userEntryRepo.save(userEntity);
        return userEntity;
    }

    public List<UserEntity> getAll(){
        return userEntryRepo.findAll();
    }

    public ResponseEntity<UserEntity> getById(ObjectId myId){
        Optional<UserEntity> userEntity = userEntryRepo.findById(myId);
        if(userEntity.isPresent()){
            return new ResponseEntity<>(userEntity.get(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);

    }


    public  Boolean deleteById(ObjectId myId){
        if(userEntryRepo.findById(myId).isPresent()){
            userEntryRepo.deleteById(myId);
            return true;
        }
        return false;
    }
    public UserEntity findByUserName(String userName) {
        return userEntryRepo.findByUserName(userName);
    }}
