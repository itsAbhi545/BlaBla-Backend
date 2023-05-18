package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(columnDefinition = "varchar(100) not null", unique = true)
    String name;

    Integer seats;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "vehicleCompanyId")
    VehicleCompany vehicleCompany;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    List<VehicleDetails> vehicleDetailsList;
}
