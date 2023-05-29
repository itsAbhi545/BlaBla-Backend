package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public interface RideRepo extends JpaRepository<Ride, Integer> {
    List<Ride> findRideBySourceAndDestination(String source, String Destination);

    @Query(
value = "select * From ride r " +
        "where (ST_Distance_Sphere(point(:#{#rideDto.source_longitude},  :#{#rideDto.source_latitude}),point(r.source_longitude, r.source_latitude)) <= :range) " +
        "And (ST_Distance_Sphere(point(:#{#rideDto.destination_longitude},  :#{#rideDto.destination_latitude}),point(r.destination_longitude, " +
        "r.destination_latitude)) <= :range) And (r.set_price >= :minPrice) And (r.set_price <= :maxPrice) And (r.time >= :minTime) And (r.time <= :maxTime) " +
        "And (r.date = :#{#rideDto.date}) And ((r.passengers_count - r.seat_booked) >= :#{#rideDto.passengers_count}) "
, nativeQuery = true
)
    Page<Ride> findAllRidesNearLocation(Pageable pageable, RideDto rideDto , @Param("range") Integer range,
                                        String minPrice, String maxPrice, Time minTime, Time maxTime);
}
