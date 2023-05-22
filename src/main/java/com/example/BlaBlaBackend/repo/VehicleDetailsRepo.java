package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface VehicleDetailsRepo extends JpaRepository<VehicleDetails,Integer> {
    List<VehicleDetails> findByUser(User user);

    @Transactional
    void deleteVehicleDetailsByUserAndVehicle(User user, Vehicle vehicle);
}
