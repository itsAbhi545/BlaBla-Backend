package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.Booking;
import com.example.BlaBlaBackend.service.BookingService;
import com.example.BlaBlaBackend.service.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService bookingService;
    private final RideService rideService;
    private final JwtProvider jwtProvider;

    public BookingController(BookingService bookingService, RideService rideService, JwtProvider jwtProvider) {
        this.bookingService = bookingService;
        this.rideService = rideService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/book/ride")
    public ApiResponse bookRide(@RequestBody Booking booking, @RequestHeader String Authorization, Principal principal){
        Booking bookingDetail = bookingService.rideBookByUser(Authorization.substring(7),booking,principal.getName());

        return ApiResponse.builder().data(bookingDetail).message("Ride Booked Successfully!!!")
                .httpStatus(HttpStatus.valueOf(200)).build();
    }
    @GetMapping("/get/booking-detail/{id}")
    public Booking getBooKingDetail(@PathVariable Integer id){
        return bookingService.findBookingByBookingId(id);
    }

    //route for accepting/declining booking request
    @PostMapping("/accept/ride/{bookingId}")
    public ApiResponse acceptRide(@PathVariable Integer bookingId,@RequestHeader String Authorization,@RequestParam String isAccepted){
        return bookingService.acceptRideByDriver(Authorization.substring(7),bookingId,isAccepted);
    }
    @GetMapping("/user/booking-history")
    public List<Booking> getBookingHistory(@RequestHeader String Authorization){
        String uid = jwtProvider.getUsernameFromToken(Authorization.substring(7));
        return bookingService.getBookingHistory(Integer.parseInt(uid));
    }
}
