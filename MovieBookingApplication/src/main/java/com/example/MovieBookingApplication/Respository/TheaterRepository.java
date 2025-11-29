package com.example.MovieBookingApplication.Respository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MovieBookingApplication.Entity.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    
    Optional<List<Theater>> findByTheaterLocation(String theaterLocation);
}
