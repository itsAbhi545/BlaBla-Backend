package com.example.BlaBlaBackend.util;

import com.example.BlaBlaBackend.Dto.RideDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Helper {
    public static String findExtension(String path){
        for(int i=path.length()-1;i>=0;i--){
            if(path.charAt(i)=='.') return path.substring(i);
        }
        //control never reach here
        return ".";
    }
    public static String extractUUid(String imageUrl){
        int idx = imageUrl.indexOf('.');
        return imageUrl.substring(8,idx);
    }
    public static RideDto findLatLogByName(RideDto rideDto) throws JsonProcessingException {
        System.out.println("\u001B[44m" + "control here "+ rideDto.getSource_latitude() + "\u001B[0m");
        if(rideDto.getSource_latitude() != null && !rideDto.getSource_latitude().equals(""))
            return rideDto;
        System.out.println("\u001B[44m" + "control here "+ rideDto.getSource_latitude() + "\u001B[0m");
        if(rideDto.getAbout_ride() == "") {
            rideDto.setAbout_ride(String.format("Heading from %s to %s . Looking for companions.",rideDto.getSource(),rideDto.getDestination() ));
        }
        String[] location = new String[2];
        location[0] = rideDto.getSource();
        location[1]  = rideDto.getDestination();
//        String url = "https://www.google.com/maps/dir/" + pickUp + "/" + drop;
        for (int i = 0; i < 2; i++) {
            String url = "https://photon.komoot.io/api/?lang=en&limit=5&q=" + location[i];
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            System.out.println("coordinate = " + root.get("features").get(0).get("geometry").get("coordinates"));
            String result = root.get("features").get(0).get("geometry").get("coordinates").get(0).toString();
            if(i == 0) {
                rideDto.setSource_longitude(result);
            }else rideDto.setDestination_longitude(result);
            result = root.get("features").get(0).get("geometry").get("coordinates").get(1).toString();
            if(i == 0) {
                rideDto.setSource_latitude(result);
            }else rideDto.setDestination_latitude(result);
        }
        return rideDto;

    }
}
