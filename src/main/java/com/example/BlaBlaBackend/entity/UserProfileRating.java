package com.example.BlaBlaBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserProfileRating {
    @Id
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "riderId")
    User rider;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    User driver;

    Integer rating;

}
