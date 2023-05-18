package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "VehicleCompany")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "varchar(100) not null", unique = true)
    String vehicleCompanyName;

    @OneToMany(mappedBy = "vehicleCompany", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    List<Vehicle> vehicleList;
}
