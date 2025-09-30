package com.example.civic_issues.services.adminService;

import com.example.civic_issues.entities.AdminPOJO;
import com.example.civic_issues.repository.adminRepo.AdminRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@SpringBootTest
class AdminServicesTest {

    @Autowired
    private AdminRepository adminRepository;
    @Test
    void getAdminsNearBy() {

        adminRepository.deleteAll();

        AdminPOJO admin1 = new AdminPOJO();
        admin1.setUserName("admin1");
        admin1.setLocation(new GeoJsonPoint(77.5946, 12.9716)); // Bangalore
        adminRepository.save(admin1);

        AdminPOJO admin2 = new AdminPOJO();
        admin2.setUserName("admin2");
        admin2.setLocation(new GeoJsonPoint(77.6090, 12.9352)); // Nearby
        adminRepository.save(admin2);

        AdminPOJO admin3 = new AdminPOJO();
        admin3.setUserName("admin3");
        admin3.setLocation(new GeoJsonPoint(77.4000, 12.9000)); // Far away
        adminRepository.save(admin3);

        GeoJsonPoint point = new GeoJsonPoint(77.5946, 12.9716); // Search center
        Distance distance = new Distance(5, Metrics.KILOMETERS);

        List<AdminPOJO> nearbyAdmins = adminRepository.findByLocationNear(point, distance);

        assertEquals(2, nearbyAdmins.size());
    }
}