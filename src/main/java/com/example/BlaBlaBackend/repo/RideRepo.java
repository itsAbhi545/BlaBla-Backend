package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepo extends JpaRepository<Ride, Integer> {
    List<Ride> findRideBySourceAndDestination(String source, String Destination);

    @Query(
            value = "select * From ride r" +
                    " where (ST_Distance_Sphere(point(:#{#rideDto.source_latitude},  :#{#rideDto.source_longitude}),point(r.source_latitude, r.source_longitude)) <= :range)" +
                    "And (ST_Distance_Sphere(point(:#{#rideDto.destination_latitude},  :#{#rideDto.destination_longitude}),point(r.destination_latitude, " +
                    "r.destination_longitude)) <= :range) And (r.set_price >= :minPrice) And (r.set_price <= :maxPrice)"
            , nativeQuery = true
    )
    List<Ride> findAllRidesNearLocation(RideDto rideDto , @Param("range") Integer range, String minPrice, String maxPrice);
}
