package com.ticketbooking.controller;

import com.ticketbooking.dto.TheaterDto;
import com.ticketbooking.service.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    @PostMapping
    public ResponseEntity<TheaterDto> createTheater(@Valid @RequestBody TheaterDto theaterDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theaterService.createTheater(theaterDto));
    }
}
