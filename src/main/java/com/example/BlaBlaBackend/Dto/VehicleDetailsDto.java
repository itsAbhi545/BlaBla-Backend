package com.example.BlaBlaBackend.Dto;

import com.example.BlaBlaBackend.TrimNullValidator.Trim;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Trim
public class VehicleDetailsDto {

    String numberPlate;
    String color;
    String name;
    Integer seats;
    String vehicleCompanyName;
    Integer vehicleCompanyId;
    String fuelType;

}
