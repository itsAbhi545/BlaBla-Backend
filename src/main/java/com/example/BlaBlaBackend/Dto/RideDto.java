package com.example.BlaBlaBackend.Dto;


import com.example.BlaBlaBackend.customAnnotation.Trim;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {
        private int id;
        @Trim
        private String source;
        @Trim
        private String destination;

        @Trim
        private String source_latitude;
        @Trim
        private String source_longitude;
        @Trim
        private String destination_latitude;

        @Trim
        private String destination_longitude;

        @Trim
        private int passengers_count;

        @Trim
        private String add_city;
        private Integer vehicle_id;
        private Integer set_price;
        @Trim
        private  String about_ride;

        @Trim
        private String date;
        @Trim
        private String time;
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
