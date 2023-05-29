package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleCompany;
import com.example.BlaBlaBackend.entity.VehicleDetails;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class VehicleGetController {

    @Autowired
    VehicleService vehicleService;
    @Autowired
    UserService userService;
    @GetMapping("/getAllBrands")
    public ApiResponse getAllBrands(){
        List<VehicleCompany> vehicleCompanyList =  vehicleService.getVehicleCompany();
        return new ApiResponse("Brand List Generated !", vehicleCompanyList, HttpStatus.CREATED);
    }
    @GetMapping("/getAllVehicles/{vehicleCompanyDetail}")
    public ApiResponse getAllBrands(@PathVariable("vehicleCompanyDetail") String vehicleCompanyDetail){
        VehicleCompany vehicleCompany = vehicleService.getVehicleCompanyByVehicleCompanyName(vehicleCompanyDetail);
        if(vehicleCompany == null) {
            vehicleCompany = vehicleService.getVehicleCompanyById(Integer.parseInt(vehicleCompanyDetail));
        }
        List<Vehicle> vehicleList = vehicleService.getVehicleByVehicleCompany(vehicleCompany);
        return new ApiResponse("Vehicle List of " + vehicleCompany.getVehicleCompanyName() + " Company Generated !", vehicleList, HttpStatus.CREATED);
    }
    @GetMapping("/getUserVehicles")
    public ApiResponse getUserVehicles(Principal principal){
        List<Vehicle> vehicleList = vehicleService.getVehicleByUser(principal.getName());
        return new ApiResponse("Vehicle List for User " + principal.getName() + " Generated !", vehicleList, HttpStatus.CREATED);
    }
}
