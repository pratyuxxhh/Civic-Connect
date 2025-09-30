package com.example.civic_issues.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "auth")
public class SigninRequestDTO {
    @Id
    private ObjectId id;
    private String userName;
    private String password;
    private String role;
}
