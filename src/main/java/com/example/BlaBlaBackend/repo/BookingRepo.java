package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepo extends JpaRepository<Booking,Integer> {
    Booking findBookingByBookingId(int bookingId);
    @Query(value="select * from booking where user_id = ?1",nativeQuery = true)
    List<Booking> getBookingHistory(int user_id);
}
