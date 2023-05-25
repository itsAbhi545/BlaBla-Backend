package com.example.BlaBlaBackend.controllers;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.Booking;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.service.BookingService;
import com.example.BlaBlaBackend.service.RideService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ApiResponse bookRide(@RequestBody Booking booking, HttpServletRequest request, Principal principal){
        Ride ride = rideService.findRideById(booking.getRideId().getId());
        if(ride==null) throw new ApiException(HttpStatus.valueOf(400),"Please Select a Valid Ride");
        int seats = ride.getSeatBooked() + booking.getSeats();
        if(seats>ride.getPassengers_count()) throw new ApiException(HttpStatus.valueOf(400),"It doesn't have enough seats");
        String uid = jwtProvider.getUsernameFromToken(request.getHeader("Authorization").substring(7));

        //setting userId on booking
        booking.setUserId(Integer.parseInt(uid));
//      // booking.setRideId(ride);
//        Ride ride1 = rideService.findRideById(booking.getRideId().getId());
//        ride1.setAbout_ride("Heading from Chicago to San Francisco. Looking for companions.");
//        rideService.saveRide(ride1);
        Booking bookingDetail = bookingService.bookRide(booking);
        //Booking booking1 = bookingService.findBookingByBookingId(bookingDetail.getBookingId());
        //updating the ride!!!
        //booking1.getUserId();
        ride.setSeatBooked(seats);
        rideService.saveRide(ride);
        booking.setRideId(ride);
        booking.getUserId().setEmail(principal.getName());
        return ApiResponse.builder().data(bookingDetail).message("Ride Booked Successfully!!!")
                .httpStatus(HttpStatus.valueOf(200)).build();
    }
    @GetMapping("/get/booking-detail/{id}")
    public Booking getBooKingDetail(@PathVariable Integer id){
        return bookingService.findBookingByBookingId(id);
    }
    //route for accepting/declining booking request
    @PostMapping("/accept/ride/{bookingId}")
    public ApiResponse acceptRide(@PathVariable Integer bookingId,HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String uid = jwtProvider.getUsernameFromToken(token);
        Booking booking = bookingService.findBookingByBookingId(bookingId);
        if(booking==null||booking.getRideId().getUser().grabCurrentUserId()!=Integer.parseInt(uid))
            throw new ApiException(HttpStatus.valueOf(400),"You are not Authorized to accept this ride!!!");
        String isAccepted =  request.getParameter("isAccepted");
        //decline the ride!!!
        if(isAccepted==null||isAccepted.equals("0")) {
            Booking booking1 = bookingService.declineRide(booking);
            return ApiResponse.builder()
                    .message("Request for ride is declined Successfully!!")
                    .httpStatus(HttpStatus.valueOf(200))
                    .data(booking1)
                    .build();
        }
        //accept the ride!!!
        Booking booking1 = bookingService.acceptRide(booking);
        return ApiResponse.builder()
                .message("Request for ride is accepted Successfully!!")
                .httpStatus(HttpStatus.valueOf(200))
                .data(booking1)
                .build();
    }
    @GetMapping("/user/booking-history")
    public List<Booking> getBookingHistory(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String uid = jwtProvider.getUsernameFromToken(token);
        return bookingService.getBookingHistory(Integer.parseInt(uid));
    }
}
