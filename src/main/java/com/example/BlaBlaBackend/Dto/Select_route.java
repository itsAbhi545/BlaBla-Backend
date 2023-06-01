package com.example.BlaBlaBackend.Dto;

import com.example.BlaBlaBackend.customAnnotation.Trim;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Select_route {
    /*
    "road_name": "Route b",
    "distance": 10,
    "duration": 30,
    "route_deatils":{
        "a": "a",
        "b": "b"
    }

     */
    @Trim
    private String road_name;
    Integer distance;
    Integer duration;
//    route_deatils
}
