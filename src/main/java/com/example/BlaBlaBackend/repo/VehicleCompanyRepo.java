package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.VehicleCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface VehicleCompanyRepo extends JpaRepository<VehicleCompany,Integer> {
    VehicleCompany getByVehicleCompanyName(String vehicleCompanyName);
}
