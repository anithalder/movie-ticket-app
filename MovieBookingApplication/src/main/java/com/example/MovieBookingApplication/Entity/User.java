package com.example.MovieBookingApplication.Entity;

import java.util.List;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String email;
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Booking> bookings;

	// Add this field
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "movie_id"))
	private Set<Movie> favoriteMovies;
}
