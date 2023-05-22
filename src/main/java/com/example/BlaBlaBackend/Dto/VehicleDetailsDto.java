package com.example.BlaBlaBackend.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleDetailsDto {
    String numberPlate;
    String color;
    String name;

    Integer seats;
    String vehicleCompanyName;
    Integer vehicleCompanyId;
    String fuelType;

}
