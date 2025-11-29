package com.example.MovieBookingApplication.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MovieBookingApplication.DTO.BookingDTO;
import com.example.MovieBookingApplication.Entity.Booking;
import com.example.MovieBookingApplication.Entity.BookingStatus;
import com.example.MovieBookingApplication.Service.BookingService;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/createBooking")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @GetMapping("/getuserbooking/{id}")
    public ResponseEntity<List<Booking>> getUserBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getUserBooking(id));
    }

    @GetMapping("/getshowbooking/{id}")
    public ResponseEntity<List<Booking>> getShowBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getShowBooking(id));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    // ----------------------------- NEW IMPLEMENTATION
    // -----------------------------
    @GetMapping("/getbookingsbyStatus/{status}")
    public ResponseEntity<List<Booking>> getBookingByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    // Add this method
    @GetMapping("/seats/{showId}")
    public ResponseEntity<Map<String, Object>> getOccupiedSeats(@PathVariable Long showId) {
        List<String> occupiedSeats = bookingService.getOccupiedSeats(showId);
        return ResponseEntity.ok(Map.of("success", true, "occupiedSeats", occupiedSeats));
    }
}
