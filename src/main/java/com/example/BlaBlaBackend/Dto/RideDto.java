package com.example.BlaBlaBackend.Dto;


import com.example.BlaBlaBackend.TrimValidator.Trim;
import com.example.BlaBlaBackend.TrimValidator.TrimConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

//@({"source","destination","about_ride"})

public class RideDto {

        private int id;

        @NotEmpty
       @Trim
        private String source;
        @Trim
        @NotEmpty
        private String destination;
        @Trim
        @NotEmpty
        private String source_latitude;
        @Trim
        @NotEmpty
        private String source_longitude;
        @Trim
        @NotEmpty
        private String destination_latitude;
        @Trim
        @NotEmpty
        private String destination_longitude;
        @Min(1)
        private int passengers_count;
        @Trim
        @NotEmpty
        private String add_city;

        private Integer vehicle_id;
        @NotEmpty
        private Integer set_price;
        @Trim
        @NotEmpty
        private  String about_ride;

        @Trim
        private String date;
        @JsonSerialize(using = LocalTimeSerializer.class)
        @JsonDeserialize(using = LocalTimeDeserializer.class)
        private LocalTime time;
        private Select_route selectRoute;

        @Override
        public String toString() {
                return "RideDto{" +
                        "id=" + id +
                        ", source='" + source + '\'' +
                        ", destination='" + destination + '\'' +
                        ", source_latitude='" + source_latitude + '\'' +
                        ", source_longitude='" + source_longitude + '\'' +
                        ", destination_latitude='" + destination_latitude + '\'' +
                        ", destination_longitude='" + destination_longitude + '\'' +
                        ", passengers_count=" + passengers_count +
                        ", add_city='" + add_city + '\'' +
                        ", vehicle_id=" + vehicle_id +
                        ", set_price=" + set_price +
                        ", about_ride='" + about_ride + '\'' +
                        ", date='" + date + '\'' +
                        ", time='" + time + '\'' +
                        ", selectRoute=" + selectRoute +
                        '}';
        }
}
