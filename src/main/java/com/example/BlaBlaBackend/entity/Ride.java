package com.example.BlaBlaBackend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    private int id;

    @NotNull
    private String source;
    @NotNull
    private String destination;

    private String source_latitude;
    private String source_longitude;
    private String destination_latitude;

    private String destination_longitude;


    private int passengers_count;
    private String add_city;
    private Integer set_price;
    private  String about_ride;

    @OneToOne
    @JoinColumn(name ="vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;
    @CreationTimestamp
    private LocalDateTime dateCreated;

    /*
    "select_route": {
    "road_name": "Route b",
    "distance": 10,
    "duration": 30,
    "route_deatils":{
        "a": "a",
        "b": "b"
    }
  }
     */





}