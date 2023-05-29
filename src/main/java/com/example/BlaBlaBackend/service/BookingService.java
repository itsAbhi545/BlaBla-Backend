package com.example.BlaBlaBackend.service;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.Exceptionhandling.ApiException;
import com.example.BlaBlaBackend.config.JwtProvider;
import com.example.BlaBlaBackend.entity.Booking;
import com.example.BlaBlaBackend.entity.Ride;
import com.example.BlaBlaBackend.repo.BookingRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepo bookingRepo;
    private final RideService rideService;
    private final JwtProvider jwtProvider;

    public BookingService(BookingRepo bookingRepo, RideService rideService, JwtProvider jwtProvider) {
        this.bookingRepo = bookingRepo;
        this.rideService = rideService;
        this.jwtProvider = jwtProvider;
    }

    public Booking bookRide(Booking booking){
        return bookingRepo.save(booking);
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
    public ApiResponse acceptRideByDriver(String token,int bookingId,String isAccepted){
        String uid = jwtProvider.getUsernameFromToken(token);
        Booking booking = bookingRepo.findBookingByBookingId(bookingId);
        if(booking==null||booking.getRideId().getUser().grabCurrentUserId()!=Integer.parseInt(uid))
            throw new ApiException(HttpStatus.valueOf(400),"You are not Authorized to accept this ride!!!");

        //decline the ride!!!
        if(isAccepted==null||isAccepted.equals("0")) {
            Booking booking1 = this.declineRide(booking);
            return ApiResponse.builder()
                    .message("Request for ride is declined Successfully!!")
                    .httpStatus(HttpStatus.valueOf(200))
                    .data(booking1)
                    .build();
        }
        //accept the ride!!!
        Booking booking1 = this.acceptRide(booking);
        return ApiResponse.builder()
                .message("Request for ride is accepted Successfully!!")
                .httpStatus(HttpStatus.valueOf(200))
                .data(booking1)
                .build();
    }
    public List<Booking> getBookingHistory(int userId){
       return bookingRepo.getBookingHistory(userId);
    }
    public Booking rideBookByUser(String token,Booking booking,String email){
        Ride ride = rideService.findRideById(booking.getRideId().getId());
        if(ride==null) throw new ApiException(HttpStatus.valueOf(400),"Please Select a Valid Ride");

        int seats = ride.getSeatBooked() + booking.getSeats();
        if(seats>ride.getPassengers_count()) throw new ApiException(HttpStatus.valueOf(400),"It doesn't have enough seats");

        String uid = jwtProvider.getUsernameFromToken(token);

        //setting userId on booking
        booking.setUserId(Integer.parseInt(uid));
//      // booking.setRideId(ride);
//        Ride ride1 = rideService.findRideById(booking.getRideId().getId());
//        ride1.setAbout_ride("Heading from Chicago to San Francisco. Looking for companions.");
//        rideService.saveRide(ride1);
        Booking bookingDetail = bookingRepo.save(booking);
        //Booking booking1 = bookingService.findBookingByBookingId(bookingDetail.getBookingId());
        //updating the ride!!!
        //booking1.getUserId();
        ride.setSeatBooked(seats);
        rideService.saveRide(ride);
        booking.setRideId(ride);
        booking.getUserId().setEmail(email);
        return bookingDetail;
    }
}
