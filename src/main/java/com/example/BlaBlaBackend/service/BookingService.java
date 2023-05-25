package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.entity.Booking;
import com.example.BlaBlaBackend.repo.BookingRepo;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepo bookingRepo;

    public BookingService(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }
    public Booking bookRide(Booking booking){
        return bookingRepo.saveAndFlush(booking);
    }
    public Booking findBookingByBookingId(int bookingId){
        return bookingRepo.findBookingByBookingId(bookingId);
    }
}
