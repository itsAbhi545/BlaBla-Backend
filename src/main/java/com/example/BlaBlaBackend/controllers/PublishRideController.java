package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.service.RideService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.VehicleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.BlaBlaBackend.util.Helper.findLatLogByName;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublishRideController {


    private final RideService rideService;
    private final VehicleService vehicleService;
    private final UserService userService;

    @PostMapping("ride/publish")
    public ApiResponse publishRide( @Valid @RequestBody RideDto rideDto, Principal principal) throws JsonProcessingException {
//        Trim trim = AnnotatedClass.class.getAnnotation(Trim.class);
//        String[] strArr = trim.value();
//        for(int i = 0; i < strArr.length; i++) {
//            System.out.println("\u001B[33m" + "value = " + strArr[i] + "\u001B[0m");
//        }

        Vehicle vehicle = vehicleService.getVehicleById(rideDto.getVehicle_id());
        // TODO
        findLatLogByName(rideDto);
        List<Ride> ridesList = rideService.findByVehicleAndDateAndTime(vehicle,rideDto.getDate() , rideDto.getTime());
        System.out.println("\u001B[34m" + ridesList.size() + "\u001B[0m");
//        if(ridesList.size() != 0){
//
//            throw new ApiException(HttpStatus.IM_USED, "Ride Already Existed With same Vehicle and Time");
//        }
        return new ApiResponse("Ride Publish Successfully",rideService.publishController(rideDto,vehicle, principal.getName()),HttpStatus.CREATED);
    }
    @GetMapping("ride/{userId}")
    public ApiResponse findRideByUser(@PathVariable Integer userId, Principal principal){
        User user = userService.findUserByEmail(principal.getName());
        if(user.grabCurrentUserId() == userId) {
            List<Ride> rideList = rideService.findRideByUser(user);
            return new ApiResponse(  "Ride List Generated For User" + user.getEmail(),rideList, HttpStatus.CREATED);
        }else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Requested User is Not Signed In");
        }
    }
    @DeleteMapping("/ride/delete/{rideId}")
    public ApiResponse deleteRideByUser(@PathVariable Integer rideId, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        rideService.deleteRideByUser(user, rideId);
        return new ApiResponse("Ride Deleted Successfully", null, HttpStatus.OK);
    }
    int[] memoArr = new int[(int) 1e6];
    @GetMapping("/ride/solution")
    public String findsolution() {
        return "hello23";
    }






}
