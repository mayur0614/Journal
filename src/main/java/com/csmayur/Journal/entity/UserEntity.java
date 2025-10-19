package com.csmayur.Journal.entity;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Document(collection = "user")
public class UserEntity {
    @Id
    private ObjectId id ;
    @Indexed(unique = true)

    @NotNull
    private String userName;
    @NotNull
    private String password;

    @DBRef
    private List<JournalEntry> journalEntries= new ArrayList<>();

    public List<String> roles ;

}
