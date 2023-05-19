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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class VehicleController {
    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ApiResponse apiResponse;

    @PostMapping("/addVehicle")
    public ApiResponse addVehicle(@RequestBody VehicleDetailsDto vehicleDetailsDto, HttpServletRequest request, Principal principal) {
        try {
            Integer vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            try{
                User user = userService.findUserByEmail(principal.getName());
                VehicleDetails vehicleDetails = new VehicleDetails();
                BeanUtils.copyProperties(vehicleDetailsDto, vehicleDetails);
                vehicleDetails.setUser(user);
                vehicleDetails.setVehicle(vehicle);
                vehicleDetails = vehicleService.saveVehicleDetails(vehicleDetails);
                return new ApiResponse("Vehicle Added successfully", vehicleDetails, HttpStatus.CREATED);
            }catch (Exception e) {
                String message = "";
                if(principal != null) {
                    message = "Vehicle Table Not Exist Or Duplicate Number Plate Found";
                }else {
                    message = "User Is Not Authenticated";
                }
                return new ApiResponse(message, null, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e) {
            return new ApiResponse("Please Select a Vehicle to continue", null, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/deleteVehicle")
    public ApiResponse deleteVehicle(Principal principal, @RequestParam("vehicleId")@ModelAttribute Integer[] vehicleIdArray) {
        try {
            Arrays.sort(vehicleIdArray);
            log.info("VehicleIdArray = {}");
            for(Integer vehicleId : vehicleIdArray) {
                System.out.println("val= " + vehicleId);
            }


            User user = userService.findUserByEmail(principal.getName());
            List<VehicleDetails> vehicleDetailsList = vehicleService.getVehicleDetailsByUser(user);
            for(VehicleDetails vehicleDetails : vehicleDetailsList) {
                Integer index = Arrays.binarySearch(vehicleIdArray, vehicleDetails.getVehicle().getId());
                if(index >= 0) {
                    Vehicle vehicle = vehicleService.getVehicleById(vehicleIdArray[index]);
                    vehicleService.deleteVehicleDetailsByUserAndVehicle(user, vehicle);
                }
            }
            return new ApiResponse("User Deleted Following Vehicle SuccessFully : ", null , HttpStatus.OK);

        } catch (Exception e) {
            return new ApiResponse("Vehicle Does Not Added to The User", null, HttpStatus.BAD_REQUEST);
        }
    }


}
