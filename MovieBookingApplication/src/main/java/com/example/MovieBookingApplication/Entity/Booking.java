package com.example.MovieBookingApplication.Entity;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer numberOfSeats;
	private LocalDateTime bookingTime;
	private Double price;

	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "booking_seat_numbers", joinColumns = @JoinColumn(name = "booking_id"))
	@Column(name = "seat_number")
	private List<String> seatNumbers;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "show_id", nullable = false)
	private Show show;
}
