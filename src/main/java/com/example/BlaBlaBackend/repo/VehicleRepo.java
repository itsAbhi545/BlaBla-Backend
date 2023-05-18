package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle,Integer> {

}
