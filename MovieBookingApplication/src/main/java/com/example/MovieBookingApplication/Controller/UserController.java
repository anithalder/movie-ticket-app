package com.example.MovieBookingApplication.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.MovieBookingApplication.Entity.Booking;
import com.example.MovieBookingApplication.Entity.Movie;
import com.example.MovieBookingApplication.Entity.User;
import com.example.MovieBookingApplication.Respository.MovieRepository;
import com.example.MovieBookingApplication.Respository.UserRepository;
import com.example.MovieBookingApplication.Service.BookingService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        // Find user by email from the security context
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingService.getUserBooking(user.getId());
        return ResponseEntity.ok(Map.of("success", true, "bookings", bookings));
    }

    // Add Favorites endpoints if you implement logic for favorites later
    @GetMapping("/favorites")
    public ResponseEntity<Map<String, Object>> getFavorites() {
        // Placeholder until you add favorites logic to User entity
        return ResponseEntity.ok(Map.of("success", true, "movies", List.of()));
    }

    // Add this method
    @PostMapping("/update-favorite")
    public ResponseEntity<Map<String, Object>> toggleFavorite(@RequestBody Map<String, Long> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long movieId = payload.get("movieId");
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));

        if (user.getFavoriteMovies() == null) {
            user.setFavoriteMovies(new HashSet<>());
        }

        if (user.getFavoriteMovies().contains(movie)) {
            user.getFavoriteMovies().remove(movie);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("success", true, "message", "Removed from favorites"));
        } else {
            user.getFavoriteMovies().add(movie);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("success", true, "message", "Added to favorites"));
        }
    }

    // Update existing getFavorites endpoint
    // @GetMapping("/favorites")
    // public ResponseEntity<Map<String, Object>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
    //     User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
    //     return ResponseEntity.ok(Map.of("success", true, "movies", user.getFavoriteMovies()));
    // }
}