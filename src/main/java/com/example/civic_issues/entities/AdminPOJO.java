package com.example.civic_issues.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Data
@Document(collection = "admin")
public class AdminPOJO {
    @Id
    private ObjectId id;
    private String userName;
    private String fullName;
    private String password;
    private String address;
    private  String email;
    private String role;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;
    private String Department;
    @DBRef
    private List<WorkerPOJO> workers= new ArrayList<>();

}
