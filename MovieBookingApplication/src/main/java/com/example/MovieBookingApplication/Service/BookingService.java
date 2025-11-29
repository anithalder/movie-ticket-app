package com.example.MovieBookingApplication.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieBookingApplication.DTO.BookingDTO;
import com.example.MovieBookingApplication.Entity.Booking;
import com.example.MovieBookingApplication.Entity.BookingStatus;
import com.example.MovieBookingApplication.Entity.Show;
import com.example.MovieBookingApplication.Entity.User;
import com.example.MovieBookingApplication.Respository.BookingRepository;
import com.example.MovieBookingApplication.Respository.ShowRepository;
import com.example.MovieBookingApplication.Respository.UserRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    // -------------------- CREATE BOOKING --------------------
    public Booking createBooking(BookingDTO bookingDTO) {

        Show show = showRepository.findById(bookingDTO.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found"));

        if (!isSeatsAvailable(show.getId(), bookingDTO.getNumberOfSeats())) {
            throw new RuntimeException("Not enough seats are available");
        }

        if (bookingDTO.getSeatNumbers().size() != bookingDTO.getNumberOfSeats()) {
            throw new RuntimeException("Seat numbers and number of seats must be equal");
        }

        validateDuplicateSeats(show.getId(), bookingDTO.getSeatNumbers());

        User user = userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
        booking.setSeatNumbers(bookingDTO.getSeatNumbers());
        booking.setPrice(calculateTotalAmount(show.getPrice(), bookingDTO.getNumberOfSeats()));
        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    // -------------------- GET BOOKINGS --------------------
    public List<Booking> getUserBooking(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getShowBooking(Long showId) {
        return bookingRepository.findByShowId(showId);
    }

    // -------------------- CONFIRM BOOKING --------------------
    public Booking confirmBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending state");
        }

        // PAYMENT API would go here
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    // -------------------- CANCEL BOOKING --------------------
    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        validateCancellation(booking);
        booking.setBookingStatus(BookingStatus.CANCELLED);

        return bookingRepository.save(booking);
    }

    public void validateCancellation(Booking booking) {

        LocalDateTime showTime = booking.getShow().getShowTime();
        LocalDateTime deadlineTime = showTime.minusHours(2);

        if (LocalDateTime.now().isAfter(deadlineTime)) {
            throw new RuntimeException("Cannot cancel booking within 2 hours of showtime");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }
    }

    // -------------------- SEAT VALIDATIONS --------------------
    public boolean isSeatsAvailable(Long showId, Integer numberOfSeats) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        int bookedSeats = show.getBookings().stream()
                .filter(b -> b.getBookingStatus() != BookingStatus.CANCELLED)
                .mapToInt(Booking::getNumberOfSeats)
                .sum();

        return (show.getTheater().getTheaterCapacity() - bookedSeats) >= numberOfSeats;
    }

    public void validateDuplicateSeats(Long showId, List<String> seatNumbers) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        Set<String> occupiedSeats = show.getBookings().stream()
                .filter(b -> b.getBookingStatus() != BookingStatus.CANCELLED)
                .flatMap(b -> b.getSeatNumbers().stream())
                .collect(Collectors.toSet());

        List<String> duplicateSeats = seatNumbers.stream()
                .filter(occupiedSeats::contains)
                .collect(Collectors.toList());

        if (!duplicateSeats.isEmpty()) {
            throw new RuntimeException("Seats already booked: " + String.join(", ", duplicateSeats));
        }
    }

    public Double calculateTotalAmount(Double price, Integer numberOfSeats) {
        return price * numberOfSeats;
    }

    // -------------------- NEW METHOD FOR CONTROLLER --------------------
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByBookingStatus(status);
    }

    // Add this method
    public List<String> getOccupiedSeats(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // Combine all seat numbers from all confirmed/pending bookings for this show
        return show.getBookings().stream()
                .filter(b -> b.getBookingStatus() != BookingStatus.CANCELLED)
                .flatMap(b -> b.getSeatNumbers().stream())
                .collect(Collectors.toList());
    }

}
