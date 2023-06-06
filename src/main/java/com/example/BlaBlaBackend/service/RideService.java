package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.repo.RideRepo;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class RideService {
    @Autowired
    RideRepo rideRepo;
    @Autowired
    UserService userService;
    public Ride saveRide(Ride ride) {
       return rideRepo.save(ride);
    }
    public Ride publishController(RideDto rideDto, Vehicle vehicle, String email){
        Ride ride = new Ride();
        BeanUtils.copyProperties(rideDto, ride);

        ride.setVehicle(vehicle);
        ride.setUser(userService.findUserByEmail(email));
        return saveRide(ride);
    }
    public List<Ride> searchRide(RideDto rideDto, String minPrice, String maxPrice, Time minTime, Time maxTime, Integer order, String sortBy) {
        Integer range = 1000000;
        Pageable pageable;
        if(order == 1){
            pageable = PageRequest.of(0, 10, Sort.Direction.ASC, sortBy);
        }else {
            pageable = PageRequest.of(0, 10, Sort.Direction.DESC, sortBy);

        }

        return rideRepo.findAllRidesNearLocation(pageable, rideDto, range, minPrice, maxPrice, minTime, maxTime).getContent();
    }
    public List<Ride> findByVehicleAndDateAndTime(Vehicle vehicle, String date, LocalTime time){

        return rideRepo.findByVehicleAndDateAndTime(vehicle,date,time);
    }
    public List<Ride> findRideByUser(User user){
         return rideRepo.findByUser(user);
    }

    public void deleteRideByUser(User user , Integer rideId) {
        rideRepo.deleteByIdAndUser(rideId, user);
    }

}
