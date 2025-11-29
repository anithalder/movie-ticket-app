package com.example.MovieBookingApplication.DTO;

import java.util.List;
import lombok.Data;

@Data
public class BookingDTO {
	private Integer numberOfSeats;
	private List<String> seatNumbers;
	private Long userId; // Use Long, not User entity
	private Long showId; // Use Long, not Show entity
}
