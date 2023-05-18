package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.VehicleDetailsDto;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleCompany;
import com.example.BlaBlaBackend.entity.VehicleDetails;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.VehicleService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class VehicleController {
    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ApiResponse apiResponse;

    @PostMapping("/addVehicle")
    public ApiResponse addVehicle(@RequestBody VehicleDetailsDto vehicleDetailsDto, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Vehicle vehicle = mapper.convertValue(vehicleDetailsDto, Vehicle.class);
        VehicleDetails vehicleDetails = mapper.convertValue(vehicleDetailsDto, VehicleDetails.class);
        VehicleCompany vehicleCompany = mapper.convertValue(vehicleDetailsDto, VehicleCompany.class);

        vehicleDetails.setVehicle(vehicle);
        vehicleDetails.setUser(user);

        vehicle.setVehicleCompany(vehicleCompany);
        log.info("\u001B[44m"  + "vehicle = " +  vehicle + "\u001B[0m");
        log.info("\u001B[44m"  + "vehicleCompany = "+ vehicleCompany + "\u001B[0m");
        log.info("\u001B[44m"  + "vehicleDetails = "+ vehicleDetails + "\u001B[0m");

        vehicleService.saveVehicleDetails(vehicleDetails);



        apiResponse.setMessage("User Added Successfully");
        apiResponse.setData(vehicleDetails);
        apiResponse.setHttpStatus(HttpStatus.CREATED);

        return apiResponse;
    }

}
