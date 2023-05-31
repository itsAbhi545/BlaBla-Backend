package com.example.BlaBlaBackend.entity;

import com.example.BlaBlaBackend.customJsonDeserializer.Trim;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "VehicleCompany")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "varchar(100) not null", unique = true)
    @Trim
    String vehicleCompanyName;

    @OneToMany(mappedBy = "vehicleCompany", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
     @JsonIgnore
    List<Vehicle> vehicleList;
}
