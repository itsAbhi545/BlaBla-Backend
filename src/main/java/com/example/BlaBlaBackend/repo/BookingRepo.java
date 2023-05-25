package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepo extends JpaRepository<Booking,Integer> {
    Booking findBookingByBookingId(int bookingId);

}
