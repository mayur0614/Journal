package com.csmayur.Journal.service;

import com.csmayur.Journal.entity.JournalEntry;
import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.repository.JournalEntryRepo;
import com.csmayur.Journal.repository.UserEntryRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//simple logging fassard for java
@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;


    @Autowired
    private UserService userService ;

    @Transactional
    public void  saveEntry(JournalEntry journalEntry, String user){
        try {
            UserEntity userIdB = userService.findByUserName(user);
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            userIdB.getJournalEntries().add(saved);
            userService.saveEntry(userIdB);
        }catch(Exception e){
            throw new RuntimeException("An error occur while saving the entry ",e);
        }
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

    public Optional<JournalEntry> findJournalEntry(ObjectId myId) {
        return journalEntryRepo.findById(myId);
    }

    @Transactional
    public  void deleteById(ObjectId myId,String userName){
        try {
            UserEntity user = userService.findByUserName(userName);
            user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            userService.saveEntry(user);
            journalEntryRepo.deleteById(myId);
        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An unexpected error occusre while deleting an entry",e);
        }
    }
}

/*
controller -> service
serice -> repository(interface)
*/