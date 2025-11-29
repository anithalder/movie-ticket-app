package com.example.MovieBookingApplication.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MovieBookingApplication.DTO.RegisterRequestDTO;
import com.example.MovieBookingApplication.Entity.Booking;
import com.example.MovieBookingApplication.Entity.Show;
import com.example.MovieBookingApplication.Entity.User;
import com.example.MovieBookingApplication.Respository.BookingRepository;
import com.example.MovieBookingApplication.Respository.ShowRepository;
import com.example.MovieBookingApplication.Respository.UserRepository;
import com.example.MovieBookingApplication.Service.AuthenticationService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Admin Registration
    @PostMapping("/registeradminuser")
    public ResponseEntity<User> registerAdminUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authenticationService.registerAdminUser(registerRequestDTO));
    }

    // 2. Dashboard Stats (Matches Dashboard.jsx)
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        long totalBookings = bookingRepository.count();
        long totalUser = userRepository.count();

        // Calculate Total Revenue by summing up the price of all bookings
        // (In a real app, filter this by 'CONFIRMED' status)
        double totalRevenue = bookingRepository.findAll().stream()
                .mapToDouble(booking -> booking.getPrice() != null ? booking.getPrice() : 0.0)
                .sum();

        List<Show> activeShows = showRepository.findAll();

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalBookings", totalBookings);
        dashboardData.put("totalRevenue", totalRevenue);
        dashboardData.put("totalUser", totalUser);
        dashboardData.put("activeShows", activeShows);

        return ResponseEntity.ok(Map.of("success", true, "dashboardData", dashboardData));
    }

    // 3. List All Shows (Matches ListShows.jsx)
    @GetMapping("/all-shows")
    public ResponseEntity<Map<String, Object>> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return ResponseEntity.ok(Map.of("success", true, "shows", shows));
    }

    // 4. List All Bookings (Matches ListBookings.jsx)
    @GetMapping("/all-bookings")
    public ResponseEntity<Map<String, Object>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok(Map.of("success", true, "bookings", bookings));
    }

    // 5. Check Admin Status (Matches Layout.jsx)
    @GetMapping("/is-admin")
    public ResponseEntity<Map<String, Boolean>> isAdmin() {
        // If the user reaches here, @PreAuthorize passed, so they are an ADMIN.
        return ResponseEntity.ok(Map.of("isAdmin", true));
    }
}