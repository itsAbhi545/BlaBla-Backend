package com.example.BlaBlaBackend.entity;

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
    String numberPlate;
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
