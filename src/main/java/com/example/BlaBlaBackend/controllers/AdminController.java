package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.VehicleDetailsDto;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleCompany;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public ApiResponse addVehicle(@RequestBody VehicleDetailsDto vehicleDetailsDto, HttpServletRequest request) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Vehicle vehicle = mapper.convertValue(vehicleDetailsDto, Vehicle.class);
        VehicleCompany vehicleCompany = new VehicleCompany();

        if (vehicleDetailsDto.getVehicleCompanyName() == null && vehicleDetailsDto.getVehicleCompanyId() == null) {
            return new ApiResponse("Please Enter Company Name or Id", null, HttpStatus.CREATED);
        } else {
            if(vehicleDetailsDto.getVehicleCompanyId() != null) {
                vehicleCompany = vehicleService.getVehicleCompanyById(vehicleDetailsDto.getVehicleCompanyId());
            }else {
                vehicleCompany = vehicleService.getVehicleCompanyByVehicleCompanyName(vehicleDetailsDto.getVehicleCompanyName());
                if(vehicleCompany == null) {
                    vehicleCompany = VehicleCompany.builder().vehicleCompanyName(vehicleDetailsDto.getVehicleCompanyName()).build();
                }
            }
        }
        vehicle.setVehicleCompany(vehicleCompany);
        try{
        vehicleService.saveVehicle(vehicle);
        }catch (Exception e) {
            return new ApiResponse("Vehicle already Exist", null, HttpStatus.BAD_REQUEST);
        }
        return new  ApiResponse("Vehicle Created Successfully", null, HttpStatus.CREATED);

    }
    @PostMapping("/addBrand")
    public ApiResponse addBrand(HttpServletRequest request) {
        String brandName = request.getParameter("brandName");
        if(brandName != null) {
            VehicleCompany vehicleCompany = VehicleCompany.builder().vehicleCompanyName(brandName).build();
            return new ApiResponse("Vehicle Brand Added Successfully",
                    vehicleService.saveVehicleCompany(vehicleCompany), HttpStatus.CREATED);
        }else {
            return  new ApiResponse("Please Enter Brand Name in Request Parameters", null, HttpStatus.BAD_REQUEST);
        }
    }
}
