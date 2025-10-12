package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.JournalEntry;
import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.repository.JournalEntryRepo;
import com.csmayur.Journal.service.JournalEntryService;
import com.csmayur.Journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService ;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserEntity user = userService.findByUserId(userName);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> all = user.getJournalEntries();

        if (all == null || all.isEmpty()) {
            return new ResponseEntity<>("No journal entries found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping
    public  ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry,userName);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED) ;
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryByIdOfUser(@PathVariable ObjectId myId){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        UserEntity user = userService.findByUserId(userName);


        List<JournalEntry> collect = user.getJournalEntries().stream().filter( x -> x.getId().equals(myId)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            Optional<JournalEntry> entry = journalEntryService.findJournalEntry(myId);
            if(entry.isPresent()){
                return new ResponseEntity<>(entry.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


        @DeleteMapping("id/{myId}")
        public Boolean deleteEntryById(@PathVariable ObjectId myId){
            Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            UserEntity user = userService.findByUserId(userName);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            if(removed) {
                userService.saveEntry(user);
                journalEntryService.deleteById(myId, userName);
                return true ;
            }
            return false ;
        }

            @PutMapping("id/{myId}")
            public ResponseEntity<?> updateJournalById(@RequestBody JournalEntry j ,@PathVariable ObjectId myId){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String userName = authentication.getName();
                UserEntity user = userService.findByUserId(userName);


                List<JournalEntry> old = user.getJournalEntries().stream().filter( x -> x.getId().equals(myId)).collect(Collectors.toList());
                if(!old.isEmpty()){
                    Optional<JournalEntry> journalEntry = journalEntryService.findJournalEntry(myId);
                    if(journalEntry.isPresent()){
                        JournalEntry journalEntry1 = journalEntry.get();
                        journalEntry1.setTitle(j.getTitle());
                        journalEntry1.setContent(j.getContent());
                        journalEntryService.saveEntry(journalEntry1);
                        return  new ResponseEntity<>("updated ",HttpStatus.OK);
                    }

                }

                return new ResponseEntity<>(HttpStatus.OK);
            }


}
