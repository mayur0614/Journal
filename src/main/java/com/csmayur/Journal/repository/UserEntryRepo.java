package com.csmayur.Journal.repository;

import com.csmayur.Journal.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntryRepo extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByUserName(String userName);
}
