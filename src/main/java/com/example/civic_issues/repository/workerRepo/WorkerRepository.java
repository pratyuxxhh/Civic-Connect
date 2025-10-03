package com.example.civic_issues.repository.workerRepo;

import com.example.civic_issues.controller.worker.Worker;
import com.example.civic_issues.entities.WorkerPOJO;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


@Component
public interface WorkerRepository extends MongoRepository<WorkerPOJO, ObjectId> {
    WorkerPOJO findByUserName(String userName);
    boolean existsByUserName(String userName);
    List<WorkerPOJO> findByDepartment(String department);
    List<WorkerPOJO> findByDepartmentAndIsHired(String department, Boolean isHired);
    List<WorkerPOJO> findByDepartmentAndIsBusy(String department, Boolean isBusy);
    List<WorkerPOJO> findByDepartmentAndIsHiredAndIsBusy(String department, Boolean isHired, Boolean isBusy);
    List<Worker> findByLocationNear(GeoJsonPoint point, Distance distance);

}
