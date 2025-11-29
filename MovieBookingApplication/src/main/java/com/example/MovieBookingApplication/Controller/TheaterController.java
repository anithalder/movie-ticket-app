package com.example.MovieBookingApplication.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.MovieBookingApplication.DTO.TheaterDTO;
import com.example.MovieBookingApplication.Entity.Theater;
import com.example.MovieBookingApplication.Service.TheaterService;

@RestController
@RequestMapping("/api/theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Theater> addTheater(@RequestBody TheaterDTO theaterDTO) {
        return ResponseEntity.ok(theaterService.addTheater(theaterDTO));
    }

    @GetMapping("/location")
    public ResponseEntity<List<Theater>> getTheaterByLocation(@RequestParam String location) {
        return ResponseEntity.ok(theaterService.getTheaterByLocation(location));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable Long id, @RequestBody TheaterDTO theaterDTO) {
        return ResponseEntity.ok(theaterService.updateTheater(id, theaterDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok().build();
    }
}
