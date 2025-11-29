package com.example.MovieBookingApplication.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MovieBookingApplication.DTO.ShowDTO;
import com.example.MovieBookingApplication.Entity.Show;
import com.example.MovieBookingApplication.Service.ShowService;

@RestController
@RequestMapping("/api/show")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping("/create")
    public ResponseEntity<Show> createShow(@RequestBody ShowDTO showDTO) {
        return ResponseEntity.ok(showService.createShow(showDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Show>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<List<Show>> getShowsByMovie(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowsByMovie(id));
    }

    @GetMapping("/theater/{id}")
    public ResponseEntity<List<Show>> getShowsByTheater(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowsByTheater(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Show> updateShow(@PathVariable Long id, @RequestBody ShowDTO showDTO) {
        return ResponseEntity.ok(showService.updateShow(id, showDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.ok().build();
    }

    // In ShowController.java
    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(@PathVariable Long id) {
        Show show = showService.getShowById(id); // You need to add this method to ShowService too
        return ResponseEntity.ok(show);
    }

    // Matches AddShows.jsx call
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addShowsBulk(@RequestBody Map<String, Object> payload) {
        // 1. Extract Data
        Long movieId = Long.valueOf(payload.get("movieId").toString());
        Double price = Double.valueOf(payload.get("showPrice").toString());
        List<Map<String, String>> showsInput = (List<Map<String, String>>) payload.get("showsInput");

        // 2. Loop and Create
        // NOTE: Your backend requires a Theater ID, but your Frontend AddShows.jsx
        // DOES NOT send one. You must either hardcode one ID or update frontend.
        // For now, we will fetch the first theater in DB or hardcode ID 1.
        Long defaultTheaterId = 1L;

        for (Map<String, String> input : showsInput) {
            ShowDTO dto = new ShowDTO();
            dto.setMovieId(movieId);
            dto.setTheaterId(defaultTheaterId); // Hardcoded because frontend is missing it
            dto.setPrice(price);

            // Combine Date and Time strings into LocalDateTime
            // Input format from frontend might need parsing depending on <input
            // type="datetime-local">
            // This is a simplified example.
            String dateTimeStr = input.get("date") + "T" + input.get("time");
            dto.setShowTime(java.time.LocalDateTime.parse(dateTimeStr));

            showService.createShow(dto);
        }

        return ResponseEntity.ok(Map.of("success", true, "message", "Shows added successfully"));
    }

    // Add this method
}
