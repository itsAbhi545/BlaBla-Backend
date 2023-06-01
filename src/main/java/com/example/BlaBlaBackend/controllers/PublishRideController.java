package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.service.RideService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.VehicleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
public class PublishRideController {
    @Autowired
    RideService rideService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    UserService userService;
    @PostMapping("/publish-ride")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse publishRide(@Valid @RequestBody RideDto rideDto, Principal principal) {
        log.warn("RideDto =" + rideDto);
        log.error("vehicle id " + rideDto.getVehicle_id());
        Vehicle vehicle = vehicleService.getVehicleById(rideDto.getVehicle_id());
        Ride ride = new Ride();
        BeanUtils.copyProperties(rideDto, ride);
        ride.setVehicle(vehicle);
        ride.setUser(userService.findUserByEmail(principal.getName()));
        ride  = rideService.saveRide(ride);
        return new ApiResponse("Ride Publish Successfully", ride, HttpStatus.CREATED);
    }
    @GetMapping("/search-ride")
    public ApiResponse searchRide(@Valid @RequestBody RideDto rideDto) {
        List<Ride> rideList = rideService.searchRide(rideDto);
        return new ApiResponse("Ride List Generated Successfully !", rideList, HttpStatus.CREATED);
    }
}
