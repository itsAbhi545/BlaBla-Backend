package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.entity.Booking;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.repo.BookingRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
public class BookingService {
    private final BookingRepo bookingRepo;
    private final RideService rideService;

    public BookingService(BookingRepo bookingRepo, RideService rideService) {
        this.bookingRepo = bookingRepo;
        this.rideService = rideService;
    }

    public Booking bookRide(Booking booking){
        return bookingRepo.saveAndFlush(booking);
    }
    public Booking findBookingByBookingId(int bookingId){
        return bookingRepo.findBookingByBookingId(bookingId);
    }
    //function for accepting the ride by driver
    public Booking acceptRide(Booking bookingDetail) {//0--pending 1--accepted 2--declined
        if (bookingDetail.isAccepted()!=0)
            throw new ApiException(HttpStatus.valueOf(400), "You can't accept this ride!!");
        bookingDetail.setAccepted(1);
        return bookingRepo.save(bookingDetail);
    }
    //function for declining the ride by driver
    public Booking declineRide(Booking bookingDetail){
        if(bookingDetail.isAccepted()==2)
            throw new ApiException(HttpStatus.valueOf(400),"You can't decline this ride!!");
        bookingDetail.setAccepted(2);
        int seats = bookingDetail.getSeats();
        int rideId = bookingDetail.getRideId().getId();
        //updating the seats occupied in ride!!!
        Ride ride = rideService.findRideById(rideId);
        ride.setSeatBooked(ride.getSeatBooked() - seats);
        rideService.saveRide(ride);
        return bookingRepo.save(bookingDetail);
    }
}
