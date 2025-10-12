package com.csmayur.Journal.service;

import com.csmayur.Journal.entity.JournalEntry;
import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.repository.JournalEntryRepo;
import com.csmayur.Journal.repository.UserEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService ;

    public void  saveEntry(JournalEntry journalEntry, String user){
            UserEntity userIdB = userService.findByUserId(user);
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            userIdB.getJournalEntries().add(saved);
            userService.saveEntry(userIdB);
    }
    public void  saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    public ResponseEntity<JournalEntry> getJournalById(ObjectId myId){
        Optional<JournalEntry>  journalEntry = journalEntryRepo.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);

    }

    public JournalEntry findJournalEntry(ObjectId myId){
        return journalEntryRepo.findById(myId).get();
    }
    public  Boolean deleteById(ObjectId myId){
        if(journalEntryRepo.findById(myId).isPresent()){
            journalEntryRepo.deleteById(myId);

            return true;
        }
        return false;
    }
    
}

/*
controller -> service
serice -> repository(interface)
*/