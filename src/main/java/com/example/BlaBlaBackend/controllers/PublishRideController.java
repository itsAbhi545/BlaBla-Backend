package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.service.RideService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.VehicleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.example.BlaBlaBackend.util.Helper.findLatLogByName;

@RestController
@Slf4j
public class PublishRideController {
    @InitBinder
    public void initialBinderForTrimmingSpaces(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimEditor);
    }
    @Autowired
    RideService rideService;

    @Autowired
    VehicleService vehicleService;
    @Autowired
    UserService userService;
    @PostMapping("ride/publish")
    public ApiResponse publishRide( @Valid @RequestBody RideDto rideDto, Principal principal) throws JsonProcessingException {

        Vehicle vehicle = vehicleService.getVehicleById(rideDto.getVehicle_id());
        // TODO
        findLatLogByName(rideDto);
        List<Ride> ridesList = rideService.findByVehicleAndDateAndTime(vehicle,rideDto.getDate() , rideDto.getTime());
        System.out.println("\u001B[34m" + ridesList.size() + "\u001B[0m");
        if(ridesList.size() != 0){

            throw new ApiException(HttpStatus.IM_USED, "Ride Already Existed With same Vehicle and Time");
        }
        return new ApiResponse("Ride Publish Successfully",rideService.publishController(rideDto,vehicle, principal.getName()),HttpStatus.CREATED);
    }





}
