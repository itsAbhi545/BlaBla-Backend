package com.example.BlaBlaBackend.Dto;


import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {
        private int id;
        private String source;
        private String destination;

        private String source_latitude;
        private String source_longitude;
        private String destination_latitude;

        private String destination_longitude;


        private int passengers_count;

        private String add_city;
        private Integer vehicle_id;
        private Integer set_price;
        private  String about_ride;

        private String date;
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
