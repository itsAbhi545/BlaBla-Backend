package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepo extends JpaRepository<Ride, Integer> {
    List<Ride> findRideBySourceAndDestination(String source, String Destination);
}
