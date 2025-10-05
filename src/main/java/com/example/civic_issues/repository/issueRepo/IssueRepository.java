package com.example.civic_issues.repository.issueRepo;

import com.example.civic_issues.entities.IssuePOJO;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends MongoRepository<IssuePOJO, ObjectId> {
    Optional<IssuePOJO> findById(ObjectId id);
    List<IssuePOJO> findByLocationNear(GeoJsonPoint point, Distance distance);
    List<IssuePOJO> findByStatus(String status);
}
