package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.VehicleDetailsDto;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ApiResponse apiResponse;
    @PostMapping("/addVehicle")
    public ApiResponse addVehicle(@RequestBody VehicleDetailsDto vehicleDetailsDto) {
        log.info(vehicleDetailsDto.getVehicleCompanyName());
        if (vehicleDetailsDto.getVehicleCompanyName() == null && vehicleDetailsDto.getVehicleCompanyId() == null) {
            throw new ApiException( HttpStatus.BAD_REQUEST,"Please Enter Company Name or Id");
        }
        try {
            vehicleService.saveVehicleByAdmin(vehicleDetailsDto);
            return new  ApiResponse("Vehicle Created Successfully", null, HttpStatus.CREATED);
        }catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Vehicle already Exist");
        }
    }
    @PostMapping("/addBrand")
    public ApiResponse addBrand(@RequestParam(value = "brandName", required = false, defaultValue = "null") String brandName) {
        if(brandName == null) {
            VehicleCompany vehicleCompany = vehicleService.saveVehicleCompanyByAdmin(brandName);
            return new ApiResponse("Vehicle Brand Added Successfully", vehicleCompany, HttpStatus.CREATED);
        }else {
            return  new ApiResponse("Please Enter Brand Name in Request Parameters", null, HttpStatus.BAD_REQUEST);
        }
    }
}
