package com.ticketbooking.service;

import com.ticketbooking.dto.TheaterDto;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.model.Theater;
import com.ticketbooking.repository.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public List<TheaterDto> getAllTheaters() {
        return theaterRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public TheaterDto createTheater(TheaterDto dto) {
        Theater theater = new Theater();
        theater.setName(dto.getName());
        theater.setLocation(dto.getLocation());
        return toDto(theaterRepository.save(theater));
    }

    public Theater findById(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));
    }

    private TheaterDto toDto(Theater theater) {
        TheaterDto dto = new TheaterDto();
        dto.setId(theater.getId());
        dto.setName(theater.getName());
        dto.setLocation(theater.getLocation());
        return dto;
    }
}
