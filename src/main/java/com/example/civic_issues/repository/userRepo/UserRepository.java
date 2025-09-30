package com.example.civic_issues.repository.userRepo;

import com.example.civic_issues.controller.worker.Worker;
import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.entities.UserPOJO;
import com.mongodb.client.model.geojson.Point;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface UserRepository extends MongoRepository<UserPOJO, ObjectId> {
    UserPOJO findByUserName(String userName);
    boolean existsByUserName(String userName);
    List<UserPOJO> findByLocationNear(GeoJsonPoint location, Distance distance);
}
