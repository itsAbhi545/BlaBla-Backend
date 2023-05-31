package com.example.BlaBlaBackend.entity;

import com.example.BlaBlaBackend.TrimValidator.Trim;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(columnDefinition = "varchar(100) not null", unique = true)
    @Trim
    String numberPlate;
    @Trim
    String color;

    String fuelType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "vehicleId")
    Vehicle vehicle;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    User user;

}
