package com.example.MovieBookingApplication.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MovieBookingApplication.Entity.Booking;
import com.example.MovieBookingApplication.Entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByShowId(Long showId);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
}
