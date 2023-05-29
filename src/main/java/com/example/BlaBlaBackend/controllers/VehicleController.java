package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.VehicleDetailsDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleDetails;
import com.example.BlaBlaBackend.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;
    @Autowired
    ApiResponse apiResponse;

    @PostMapping("/addVehicle")
    public ApiResponse addVehicle(@RequestBody VehicleDetailsDto vehicleDetailsDto,
            @RequestParam(value = "vehicleId", required = false, defaultValue = "null") Integer vehicleId, Principal principal) {
        if(vehicleId == null) throw new ApiException(HttpStatus.valueOf(400), "Enter Vehicle Id");
        try {
            VehicleDetails vehicleDetails = vehicleService.saveVehicleDetailsByUser(vehicleDetailsDto, vehicleId, principal);
            return new ApiResponse("Vehicle Added successfully", vehicleDetails, HttpStatus.CREATED);
        }catch (Exception e) {
            String message = "Duplicate Number Plate Found";
            throw new ApiException(HttpStatus.BAD_REQUEST, message);
        }
    }



    @DeleteMapping("/deleteVehicle")
    public ApiResponse deleteVehicle(Principal principal, @RequestParam("vehicleDetailsId")@ModelAttribute Integer[] vehicleDetailsIdArray) {
        log.info("vehicleid  = " , vehicleDetailsIdArray);
        List<VehicleDetails> deletedVehicleList = vehicleService.deleteVehicleDetailsByUserAndVehicle(principal.getName(), vehicleDetailsIdArray);
        if(deletedVehicleList.size() != 0)
        return new ApiResponse("User Deleted Following Vehicle SuccessFully : ", deletedVehicleList , HttpStatus.OK);
        else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No Vehicle Added To User");
        }
    }


}
