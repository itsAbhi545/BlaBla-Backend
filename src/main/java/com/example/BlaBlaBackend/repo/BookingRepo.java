package com.example.BlaBlaBackend.repo;

import com.example.BlaBlaBackend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepo extends JpaRepository<Booking,Integer> {

}
