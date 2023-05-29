package com.example.BlaBlaBackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Ride {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String source;
    @NotNull
    private String destination;
    @JsonIgnore
    private String source_latitude;
    @JsonIgnore
    private String source_longitude;
    @JsonIgnore
    private String destination_latitude;
    @JsonIgnore
    private String destination_longitude;
    private int passengers_count;
    private String add_city;
    private Integer set_price;
    private  String about_ride;
    private int seatBooked=0;

    @OneToOne
    @JoinColumn(name ="vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
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
