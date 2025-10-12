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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService ;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){

        UserEntity user = userService.findByUserId(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all != null && all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public  ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry,@PathVariable String userName){

        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry,userName);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED) ;
    }
    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> findById(@PathVariable ObjectId myId){
        return journalEntryService.getJournalById(myId);
    }

        @DeleteMapping("id/{userName}/{myId}")
        public Boolean deleteEntryById(@PathVariable ObjectId myId,@PathVariable String userName){
            UserEntity user = userService.findByUserId(userName);
            user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            userService.saveEntry(user);
            return journalEntryService.deleteById(myId) ;
        }

        @PutMapping("id/{userName}/{myId}")
        public JournalEntry updateJournalEntry(@RequestBody JournalEntry myEntry,@PathVariable ObjectId myId,@PathVariable String userName) {
            JournalEntry j = journalEntryService.findJournalEntry(myId);
            j.setTitle(myEntry.getTitle());
            j.setContent(myEntry.getContent());
            j.setId(myId);
            j.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(j);
            return j ;
        }
}
