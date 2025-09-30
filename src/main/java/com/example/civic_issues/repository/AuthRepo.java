package com.example.civic_issues.repository;

import com.example.civic_issues.entities.SigninRequestDTO;
import com.example.civic_issues.entities.WorkerPOJO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface AuthRepo extends MongoRepository<SigninRequestDTO , ObjectId> {
    SigninRequestDTO findByUserName(String userName);
    boolean existsByUserName(String userName);

}
