package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.User;
import com.example.BlaBlaBackend.entity.Vehicle;
import com.example.BlaBlaBackend.entity.VehicleCompany;
import com.example.BlaBlaBackend.entity.VehicleDetails;
import com.example.BlaBlaBackend.repo.VehicleCompanyRepo;
import com.example.BlaBlaBackend.repo.VehicleDetailsRepo;
import com.example.BlaBlaBackend.repo.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    VehicleDetailsRepo vehicleDetailsRepo;
    VehicleRepo vehicleRepo;
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
    public List<VehicleCompany> getVehicleCompany(){
        return vehicleCompanyRepo.findAll();
    }
    public Vehicle getVehicleById(Integer id) {
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
    public VehicleCompany getVehicleCompanyByVehicleCompanyName(String vehicleCompanyName) {
        return vehicleCompanyRepo.getByVehicleCompanyName(vehicleCompanyName);
    }


    // Delete
    public void deleteVehicleDetailsByUserAndVehicle(User user, Vehicle vehicle) {
        vehicleDetailsRepo.deleteVehicleDetailsByUserAndVehicle(user, vehicle);
    }

}
