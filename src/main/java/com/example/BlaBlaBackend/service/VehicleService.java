package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Dto.VehicleDetailsDto;
import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleCompany;
import com.example.BlaBlaBackend.entity.VehicleDetails;
import com.example.BlaBlaBackend.repo.VehicleCompanyRepo;
import com.example.BlaBlaBackend.repo.VehicleDetailsRepo;
import com.example.BlaBlaBackend.repo.VehicleRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class VehicleService {
    @Autowired
    VehicleDetailsRepo vehicleDetailsRepo;
    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired
    VehicleCompanyRepo vehicleCompanyRepo;
    @Autowired
    UserService userService;

    public VehicleDetails saveVehicleDetails(VehicleDetails vehicleDetails){
        return vehicleDetailsRepo.save(vehicleDetails);
    }
    public VehicleDetails saveVehicleDetailsByUser(VehicleDetailsDto vehicleDetailsDto, Integer vehicleId, Principal principal) {
        Vehicle vehicle = getVehicleById(vehicleId);

        User user = userService.findUserByEmail(principal.getName());
        VehicleDetails vehicleDetails = new VehicleDetails();
        BeanUtils.copyProperties(vehicleDetailsDto, vehicleDetails);
        vehicleDetails.setUser(user);
        vehicleDetails.setVehicle(vehicle);
        saveVehicleDetails(vehicleDetails);
        return vehicleDetails;
    }
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }
    public Vehicle saveVehicleByAdmin(VehicleDetailsDto vehicleDetailsDto){

        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDetailsDto,vehicle);
        VehicleCompany vehicleCompany = new VehicleCompany();
        if(vehicleDetailsDto.getVehicleCompanyId() != null) {
            vehicleCompany = getVehicleCompanyById(vehicleDetailsDto.getVehicleCompanyId());
        }else {
            vehicleCompany = getVehicleCompanyByVehicleCompanyName(vehicleDetailsDto.getVehicleCompanyName());
            if(vehicleCompany == null) {
                vehicleCompany = VehicleCompany.builder().vehicleCompanyName(vehicleDetailsDto.getVehicleCompanyName()).build();
            }
        }
        vehicle.setVehicleCompany(vehicleCompany);
       return  vehicleRepo.save(vehicle);
    }

    public VehicleCompany saveVehicleCompany(VehicleCompany vehicleCompany) {
        return vehicleCompanyRepo.save(vehicleCompany);
    }
    public VehicleCompany saveVehicleCompanyByAdmin(String brandName){
        VehicleCompany vehicleCompany = VehicleCompany.builder().vehicleCompanyName(brandName).build();
        return vehicleCompanyRepo.save(vehicleCompany);
    }


    //Getter

    public VehicleCompany getVehicleCompanyById(Integer id) {
        return vehicleCompanyRepo.findById(id).get();
    }
    public List<VehicleCompany> getVehicleCompany(){
        return vehicleCompanyRepo.findAll();
    }
    public Vehicle getVehicleById(Integer id) {
        System.out.println("vehicle = in service " + id);
        return vehicleRepo.findById(id).get();
    }
    public List<Vehicle> getVehicleByVehicleCompany(VehicleCompany vehicleCompany) {
        return vehicleRepo.findByVehicleCompany(vehicleCompany);
    }

    public VehicleDetails getVehicleDetailsById(Integer id) {
        return vehicleDetailsRepo.findById(id).get();
    }
    public List<VehicleDetails> getVehicleDetailsByUser(User user) {
        return vehicleDetailsRepo.findByUser(user);
    }
    public List<Vehicle> getVehicleByUser(String email){

        User user = userService.findUserByEmail(email);
        List<VehicleDetails> vehicleDetailsList = getVehicleDetailsByUser(user);
        List<Vehicle> vehicleList =  new ArrayList<>();
        vehicleDetailsList.forEach(vehicleDetails -> {
            Vehicle vehicle = vehicleDetails.getVehicle();
            vehicleList.add(vehicle);
        });
        return vehicleList;
    }
    public VehicleCompany getVehicleCompanyByVehicleCompanyName(String vehicleCompanyName) {
        return vehicleCompanyRepo.getByVehicleCompanyName(vehicleCompanyName);
    }


    // Delete
    public List<VehicleDetails> deleteVehicleDetailsByUserAndVehicle(String email, Integer[] vehicleDetailsIdArray) {
            log.info("VehicleIdArray = {}", vehicleDetailsIdArray.length);
            List<VehicleDetails> deleteVehicleDetailsList = new ArrayList<>();

            for(int i = 0; i < vehicleDetailsIdArray.length; i++){
                try{
                    deleteVehicleDetailsList.add(vehicleDetailsRepo.findById(vehicleDetailsIdArray[i]).get());
                    vehicleDetailsRepo.deleteById(vehicleDetailsIdArray[i]);

                }catch (Exception e){

                }
            }
            return deleteVehicleDetailsList;
    }

}
