package com.example.MovieBookingApplication.DTO;

import lombok.Data;

@Data
public class TheaterDTO {
	private String theaterName;
	private String theaterLocation; // camelCase
	
	private Integer theaterCapacity;
	private String theaterScreenType;
}
