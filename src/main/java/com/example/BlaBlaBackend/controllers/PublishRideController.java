package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.TrimValidator.Trim;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.service.RideService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.VehicleService;
import com.example.BlaBlaBackend.util.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.sql.Time;
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
    public ApiResponse publishRide(@RequestBody @Valid RideDto rideDto, Principal principal) throws JsonProcessingException {
//        Trim trim = AnnotatedClass.class.getAnnotation(Trim.class);
//        String[] strArr = trim.value();
//        for(int i = 0; i < strArr.length; i++) {
//            System.out.println("\u001B[33m" + "value = " + strArr[i] + "\u001B[0m");
//        }

        Vehicle vehicle = vehicleService.getVehicleById(rideDto.getVehicle_id());
        // TODO
        findLatLogByName(rideDto);
        return new ApiResponse("Ride Publish Successfully",rideService.publishController(rideDto,vehicle, principal.getName()),HttpStatus.CREATED);
    }





}
