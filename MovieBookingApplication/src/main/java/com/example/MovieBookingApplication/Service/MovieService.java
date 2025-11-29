package com.example.MovieBookingApplication.Service;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieBookingApplication.DTO.MovieDTO;
import com.example.MovieBookingApplication.Entity.Movie;
import com.example.MovieBookingApplication.Respository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;


    // -------------------- ADD MOVIE --------------------
    public Movie addMovie(MovieDTO movieDTO) {

        Movie movie = new Movie();
        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setGenre(movieDTO.getGenre());
        movie.setDuration(movieDTO.getDuration());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setLanguage(movieDTO.getLanguage());

        return movieRepository.save(movie);
    }


    // -------------------- GET ALL MOVIES --------------------
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }


    // -------------------- GET MOVIES BY GENRE --------------------
    public List<Movie> getMoviesByGenre(String genre) {

        List<Movie> movies = movieRepository.findByGenre(genre);

        if (movies.isEmpty()) {
            throw new RuntimeException("No movies found for genre: " + genre);
        }
        return movies;
    }


    // -------------------- GET MOVIES BY LANGUAGE --------------------
    public List<Movie> getMoviesByLanguage(String language) {

        List<Movie> movies = movieRepository.findByLanguage(language);

        if (movies.isEmpty()) {
            throw new RuntimeException("No movies found for language: " + language);
        }
        return movies;
    }


    // -------------------- GET MOVIE BY TITLE --------------------
    public Movie getMoviesByTitle(String title) {

        return movieRepository.findByName(title)
                .orElseThrow(() -> new RuntimeException("No movie found with title: " + title));
    }


    // -------------------- UPDATE MOVIE --------------------
    public Movie updateMovie(Long id, MovieDTO movieDTO) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No movie found with ID: " + id));

        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setGenre(movieDTO.getGenre());
        movie.setDuration(movieDTO.getDuration());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setLanguage(movieDTO.getLanguage());

        return movieRepository.save(movie);
    }


    // -------------------- DELETE MOVIE --------------------
    public void deleteMovie(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No movie found with ID: " + id));

        movieRepository.delete(movie);
    }
}
