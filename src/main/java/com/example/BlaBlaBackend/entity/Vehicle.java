package com.example.BlaBlaBackend.entity;

import com.example.BlaBlaBackend.customJsonDeserializer.Trim;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Trim
    @Column(columnDefinition = "varchar(100) not null", unique = true)
    String name;

    Integer seats;


    @ManyToOne( fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "vehicleCompanyId")
    VehicleCompany vehicleCompany;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    List<VehicleDetails> vehicleDetailsList;
}
