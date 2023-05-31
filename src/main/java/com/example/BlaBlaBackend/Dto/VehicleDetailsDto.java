package com.example.BlaBlaBackend.Dto;

import com.example.BlaBlaBackend.customJsonDeserializer.Trim;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleDetailsDto {
    @Trim
    String numberPlate;
    @Trim
    String color;
    @Trim
    String name;
    Integer seats;
    @Trim
    String vehicleCompanyName;
    Integer vehicleCompanyId;
    @Trim
    String fuelType;

}
