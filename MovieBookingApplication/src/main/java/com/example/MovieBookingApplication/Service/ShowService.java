package com.example.MovieBookingApplication.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieBookingApplication.DTO.ShowDTO;
import com.example.MovieBookingApplication.Entity.Booking;
import com.example.MovieBookingApplication.Entity.Movie;
import com.example.MovieBookingApplication.Entity.Show;
import com.example.MovieBookingApplication.Entity.Theater;
import com.example.MovieBookingApplication.Respository.ShowRepository;
import com.example.MovieBookingApplication.Respository.MovieRepository;
import com.example.MovieBookingApplication.Respository.TheaterRepository;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    // CREATE SHOW
    public Show createShow(ShowDTO showDTO) {

        Movie movie = movieRepository.findById(showDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found: " + showDTO.getMovieId()));

        Theater theater = theaterRepository.findById(showDTO.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found: " + showDTO.getTheaterId()));

        Show show = new Show();
        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setMovie(movie);
        show.setTheater(theater);

        return showRepository.save(show);
    }

    // GET ALL SHOWS
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    // GET SHOWS BY MOVIE
    public List<Show> getShowsByMovie(Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);

        if (shows.isEmpty()) {
            throw new RuntimeException("No shows available for movie ID: " + movieId);
        }

        return shows;
    }

    // GET SHOWS BY THEATER
    public List<Show> getShowsByTheater(Long theaterId) {
        List<Show> shows = showRepository.findByTheaterId(theaterId);

        if (shows.isEmpty()) {
            throw new RuntimeException("No shows available for theater ID: " + theaterId);
        }

        return shows;
    }

    // UPDATE SHOW
    public Show updateShow(Long id, ShowDTO showDTO) {

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + id));

        Movie movie = movieRepository.findById(showDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found: " + showDTO.getMovieId()));

        Theater theater = theaterRepository.findById(showDTO.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found: " + showDTO.getTheaterId()));

        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setMovie(movie);
        show.setTheater(theater);

        return showRepository.save(show);
    }

    // DELETE SHOW
    public void deleteShow(Long id) {

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + id));

        if (!show.getBookings().isEmpty()) {
            throw new RuntimeException("Cannot delete show — bookings already exist!");
        }

        showRepository.delete(show);
    }

    // In ShowService.java
    // Add this method
    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));
    }
}
