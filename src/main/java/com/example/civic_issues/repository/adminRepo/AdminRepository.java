package com.example.civic_issues.repository.adminRepo;

import com.example.civic_issues.entities.AdminPOJO;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


@Component
public interface AdminRepository extends MongoRepository<AdminPOJO, ObjectId> {
    AdminPOJO findByUserName(String userName);
    boolean existsByUserName(String userName);
    List<AdminPOJO> findByLocationNear(GeoJsonPoint point, Distance distance);
}
