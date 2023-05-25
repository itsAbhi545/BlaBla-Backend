package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;
    @Min(1) @Max(4)
    private int seats;
    @Column(columnDefinition = "int default 0")
    private int isAccepted;
    @CreationTimestamp
    private LocalDateTime bookDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rideId")
    private Ride rideId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Ride getRideId() {
        return rideId;
    }

    public void setRideId(Ride rideId) {
        this.rideId = rideId;
    }
    public void setRideId(int rideId) {
        this.rideId = Ride.builder().id(rideId).build();
    }
    public User getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        User user = new User();
        user.setId(userId);
        this.userId = user;
    }
    public void setUserId(User userId) {
        this.userId = userId;
    }

    public LocalDateTime getBookDate() {
        return bookDate;
    }

    public void setBookDate(LocalDateTime bookDate) {
        this.bookDate = bookDate;
    }

    public int isAccepted() {
        return isAccepted;
    }

    public void setAccepted(int accepted) {
        isAccepted = accepted;
    }
}
