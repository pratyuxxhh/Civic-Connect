package com.example.civic_issues.entities;


import com.mongodb.client.model.geojson.Point;
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
@Document(collection = "user")
public class UserPOJO {
    @Id
    private ObjectId id;
    private String userName;
    private String fullName;
    private String password;
    private String address;
    private String role;
    private String email;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;
    @DBRef
    private List<IssuePOJO> issues = new ArrayList<>();


}
