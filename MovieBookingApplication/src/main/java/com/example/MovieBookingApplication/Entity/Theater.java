package com.example.MovieBookingApplication.Entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "theaters")
public class Theater {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String theaterName;

	private String theaterLocation;
	private Integer theaterCapacity;
	private String theaterScreenType;

	@OneToMany(mappedBy = "theater", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Show> shows;
}
