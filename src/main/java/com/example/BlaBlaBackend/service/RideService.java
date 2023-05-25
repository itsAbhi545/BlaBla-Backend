package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.repo.RideRepo;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {
    @Autowired
    RideRepo rideRepo;
    public Ride saveRide(Ride ride) {
        return rideRepo.save(ride);
    }
    public List<Ride> searchRide(RideDto rideDto) {
         return null;
    }
    public Ride findRideById(int id){return rideRepo.findRideById(id);}
}
