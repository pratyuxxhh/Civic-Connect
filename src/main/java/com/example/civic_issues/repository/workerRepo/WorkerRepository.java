package com.example.civic_issues.repository.workerRepo;

import com.example.civic_issues.entities.WorkerPOJO;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface WorkerRepository extends MongoRepository<WorkerPOJO, ObjectId> {
    WorkerPOJO findByUserName(String userName);
    boolean existsByUserName(String userName);
    List<WorkerPOJO> findByDepartment(String department);
    List<WorkerPOJO> findByDepartmentAndIsHired(String department, Boolean isHired);
    List<WorkerPOJO> findByDepartmentAndIsBusy(String department, Boolean isBusy);
    List<WorkerPOJO> findByDepartmentAndIsHiredAndIsBusy(String department, Boolean isHired, Boolean isBusy);
    List<WorkerPOJO> findByLocationNear(GeoJsonPoint point, Distance distance);
    List<WorkerPOJO> findByDepartmentAndIsHiredAndLocationNear(String department, Boolean isHired, GeoJsonPoint point, Distance distance);

}
