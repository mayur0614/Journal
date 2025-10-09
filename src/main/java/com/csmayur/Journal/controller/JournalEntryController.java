package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.JournalEntry;
import com.csmayur.Journal.repository.JournalEntryRepo;
import com.csmayur.Journal.service.JournalEntryService;
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


    @GetMapping
    public List<JournalEntry> getAll(){
        return journalEntryService.getAll();
    }

    @PostMapping
    public  ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED) ;
    }
    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> findById(@PathVariable ObjectId myId){
        return journalEntryService.getJournalById(myId);
    }

        @DeleteMapping("id/{myId}")
        public Boolean deleteEntryById(@PathVariable ObjectId myId){
            return journalEntryService.deleteById(myId) ;
        }

        @PutMapping("id/{myId}")
        public JournalEntry updateJournalEntry(@RequestBody JournalEntry myEntry,@PathVariable ObjectId myId) {
            JournalEntry j = journalEntryService.findJournalEntry(myId);
            j.setTitle(myEntry.getTitle());
            j.setContent(myEntry.getContent());
            j.setId(myId);
            j.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(j);
            return j ;
        }
}
