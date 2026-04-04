package com.ticketbooking.controller;

import com.ticketbooking.dto.SeatDto;
import com.ticketbooking.dto.ShowDto;
import com.ticketbooking.service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowDto>> getShowsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    @PostMapping
    public ResponseEntity<ShowDto> createShow(@Valid @RequestBody ShowDto showDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.createShow(showDto));
    }

    @GetMapping("/{showId}/seats")
    public ResponseEntity<List<SeatDto>> getAvailableSeats(@PathVariable Long showId) {
        return ResponseEntity.ok(showService.getAvailableSeats(showId));
    }
}
