package com.example.BlaBlaBackend.entity;


import com.example.BlaBlaBackend.customJsonDeserializer.Trim;
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
    @NotNull @Trim
    private String source;
    @NotNull @Trim
    private String destination;
    @JsonIgnore @Trim
    private String source_latitude;
    @JsonIgnore @Trim
    private String source_longitude;
    @JsonIgnore @Trim
    private String destination_latitude;
    @JsonIgnore @Trim
    private String destination_longitude;

    private int passengers_count;
    @Trim
    private String add_city;
    private Integer set_price;
    @Trim
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
