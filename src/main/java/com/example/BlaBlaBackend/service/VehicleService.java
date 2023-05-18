package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleCompany;
import com.example.BlaBlaBackend.entity.VehicleDetails;
import com.example.BlaBlaBackend.repo.VehicleCompanyRepo;
import com.example.BlaBlaBackend.repo.VehicleDetailsRepo;
import com.example.BlaBlaBackend.repo.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    VehicleDetailsRepo vehicleDetailsRepo;
    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired
    VehicleCompanyRepo vehicleCompanyRepo;
    public VehicleDetails saveVehicleDetails(VehicleDetails vehicleDetails) {
        return vehicleDetailsRepo.save(vehicleDetails);
    }
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }
    public VehicleCompany saveVehicleCompany(VehicleCompany vehicleCompany) {
        return vehicleCompanyRepo.save(vehicleCompany);
    }


    //Getter

    public VehicleCompany getVehicleCompanyById(Integer id) {
        return vehicleCompanyRepo.findById(id).get();
    }
    public Vehicle getVehicleById(Integer id) {
        return vehicleRepo.findById(id).get();
    }
    public VehicleCompany getVehicleCompanyByVehicleCompanyName(String vehicleCompanyName) {
        return vehicleCompanyRepo.getByVehicleCompanyName(vehicleCompanyName);
    }

}
