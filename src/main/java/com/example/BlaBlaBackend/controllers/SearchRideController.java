package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.service.RideService;
import com.example.BlaBlaBackend.service.VehicleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.List;

import static com.example.BlaBlaBackend.util.Helper.findLatLogByName;
@RestController
@Slf4j
public class SearchRideController {



    @Autowired
    RideService rideService;

    @Autowired
    VehicleService vehicleService;
    @GetMapping("/search-ride")
    public ApiResponse searchRide(@RequestBody RideDto rideDto,
                                  @RequestParam(value = "min-price", required=false, defaultValue = "0")String minPrice,
                                  @RequestParam(value = "max-price", required=false, defaultValue = "10000000")String maxPrice,
                                  @RequestParam(value = "sortBy", required=false, defaultValue = "time")  String sortBy,
                                  @RequestParam(value = "order", required=false, defaultValue = "1")String order,
                                  @RequestParam(value = "min-time", required=false, defaultValue = "00:00:00") Time minTime,
                                  @RequestParam(value = "max-time", required=false, defaultValue = "23:59:59")Time maxTime
    ) throws JsonProcessingException {
        // TODO
        findLatLogByName(rideDto);

        List<Ride> rideList = rideService.searchRide(rideDto, minPrice, maxPrice,minTime , maxTime,Integer.parseInt(order), sortBy);
        return new ApiResponse("Ride List Generated Successfully !", rideList, HttpStatus.CREATED);
    }
}
