package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.Dto.RideDto;
import com.example.BlaBlaBackend.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepo extends JpaRepository<Ride, Integer> {
    List<Ride> findRideBySourceAndDestination(String source, String Destination);

    @Query(
            value = "CREATE FUNCTION east_or_west (\n" +
                    "\t@long DECIMAL(9,6)\n" +
                    ")\n" +
                    "RETURNS CHAR(4) AS\n" +
                    "BEGIN\n" +
                    "\tDECLARE @return_value CHAR(4);\n" +
                    "\tSET @return_value = 'same';\n" +
                    "    IF (@long > 0.00) SET @return_value = 'east';\n" +
                    "    IF (@long < 0.00) SET @return_value = 'west';\n" +
                    " \n" +
                    "    RETURN @return_value\n" +
                    "END;" +
                    "Select * from Ride" , nativeQuery = true
    )
    Integer calculateDistance(RideDto rideDto);
}
