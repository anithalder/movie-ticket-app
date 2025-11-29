package com.example.MovieBookingApplication.Entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "movies")
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private String genre;
	private Integer duration; // in minutes
	private LocalDate releaseDate;

	private String language;

	@OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Show> shows;
}
