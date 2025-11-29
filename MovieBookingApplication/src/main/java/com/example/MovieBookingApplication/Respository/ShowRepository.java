package com.example.MovieBookingApplication.Respository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MovieBookingApplication.Entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {

    // Correct — return list directly
    List<Show> findByMovieId(Long movieId);

    // Correct — return list directly
    List<Show> findByTheaterId(Long theaterId);

    // ❌ DO NOT override findById — already provided by JpaRepository
}
