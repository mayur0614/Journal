package com.csmayur.Journal.controller;

import com.csmayur.Journal.entity.JournalEntry;
import com.csmayur.Journal.entity.UserEntity;
import com.csmayur.Journal.service.JournalEntryService;
import com.csmayur.Journal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // ✅ HEALTH CHECK ENDPOINT
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check endpoint hit at {}", LocalDateTime.now());
        return ResponseEntity.ok("✅ Journal service is running fine at " + LocalDateTime.now());
    }

    // ✅ GET all journal entries for authenticated user
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Fetching all journal entries for user: {}", userName);

            UserEntity user = userService.findByUserName(userName);
            if (user == null) {
                log.warn("User '{}' not found in database", userName);
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            List<JournalEntry> all = user.getJournalEntries();
            if (all == null || all.isEmpty()) {
                log.info("No journal entries found for user '{}'", userName);
                return new ResponseEntity<>("No journal entries found", HttpStatus.NOT_FOUND);
            }

            log.info("Found {} journal entries for user '{}'", all.size(), userName);
            return new ResponseEntity<>(all, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error fetching journal entries", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ CREATE a new journal entry
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Creating journal entry for user: {}", userName);

            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry, userName);
            log.info("Journal entry created successfully for user: {}", userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error creating journal entry", e);
            return new ResponseEntity<>("Error creating journal entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ GET journal entry by ID
    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryByIdOfUser(@PathVariable ObjectId myId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Fetching journal entry with ID: {} for user: {}", myId, userName);

            UserEntity user = userService.findByUserName(userName);
            if (user == null) {
                log.warn("User '{}' not found while trying to fetch journal by ID", userName);
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            List<JournalEntry> collect = user.getJournalEntries().stream()
                    .filter(x -> x.getId().equals(myId))
                    .collect(Collectors.toList());

            if (!collect.isEmpty()) {
                Optional<JournalEntry> entry = journalEntryService.findJournalEntry(myId);
                if (entry.isPresent()) {
                    log.info("Journal entry found for ID: {}", myId);
                    return new ResponseEntity<>(entry.get(), HttpStatus.OK);
                }
            }

            log.warn("No journal entry found for ID: {}", myId);
            return new ResponseEntity<>("Journal not found", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("Error fetching journal entry by ID", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ DELETE journal entry by ID
    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Deleting journal entry with ID: {} for user: {}", myId, userName);

            UserEntity user = userService.findByUserName(userName);
            if (user == null) {
                log.warn("User '{}' not found while trying to delete journal entry", userName);
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            if (removed) {
                userService.saveEntry(user);
                journalEntryService.deleteById(myId, userName);
                log.info("Journal entry deleted successfully for user: {}", userName);
                return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
            }

            log.warn("No journal entry found with ID: {} to delete", myId);
            return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("Error deleting journal entry", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ UPDATE journal entry by ID
    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalById(@RequestBody JournalEntry j, @PathVariable ObjectId myId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            log.info("Updating journal entry with ID: {} for user: {}", myId, userName);

            UserEntity user = userService.findByUserName(userName);
            if (user == null) {
                log.warn("User '{}' not found while trying to update journal", userName);
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            List<JournalEntry> old = user.getJournalEntries().stream()
                    .filter(x -> x.getId().equals(myId))
                    .collect(Collectors.toList());

            if (!old.isEmpty()) {
                Optional<JournalEntry> journalEntry = journalEntryService.findJournalEntry(myId);
                if (journalEntry.isPresent()) {
                    JournalEntry journalEntry1 = journalEntry.get();
                    journalEntry1.setTitle(j.getTitle());
                    journalEntry1.setContent(j.getContent());
                    journalEntryService.saveEntry(journalEntry1);
                    log.info("Journal entry updated successfully for user: {}", userName);
                    return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
                }
            }

            log.warn("No journal entry found for ID: {} to update", myId);
            return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("Error updating journal entry", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
