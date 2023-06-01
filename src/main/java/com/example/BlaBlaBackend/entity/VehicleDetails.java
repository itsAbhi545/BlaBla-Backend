package com.example.BlaBlaBackend.entity;

import com.example.BlaBlaBackend.customAnnotation.Trim;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Trim
    @Column(columnDefinition = "varchar(100) not null", unique = true)
    String numberPlate;
    @Trim
    String color;
    @Trim
    String fuelType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "vehicleId")
    Vehicle vehicle;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    User user;

}
