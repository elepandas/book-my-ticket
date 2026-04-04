package com.ticketbooking.service;

import com.ticketbooking.dto.SeatDto;
import com.ticketbooking.dto.ShowDto;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.model.Movie;
import com.ticketbooking.model.Seat;
import com.ticketbooking.model.Show;
import com.ticketbooking.model.Theater;
import com.ticketbooking.model.enums.SeatStatus;
import com.ticketbooking.repository.MovieRepository;
import com.ticketbooking.repository.SeatRepository;
import com.ticketbooking.repository.ShowRepository;
import com.ticketbooking.repository.TheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    public ShowService(ShowRepository showRepository, MovieRepository movieRepository,
                       TheaterRepository theaterRepository, SeatRepository seatRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
    }

    public List<ShowDto> getShowsByMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id: " + movieId);
        }
        return showRepository.findByMovieId(movieId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ShowDto createShow(ShowDto dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + dto.getMovieId()));
        Theater theater = theaterRepository.findById(dto.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + dto.getTheaterId()));

        Show show = new Show();
        show.setMovie(movie);
        show.setTheater(theater);
        show.setShowTime(dto.getShowTime());
        show.setPrice(dto.getPrice());
        show = showRepository.save(show);
        return toDto(show);
    }

    public List<SeatDto> getAvailableSeats(Long showId) {
        if (!showRepository.existsById(showId)) {
            throw new ResourceNotFoundException("Show not found with id: " + showId);
        }
        return seatRepository.findByShowIdAndStatus(showId, SeatStatus.AVAILABLE)
                .stream().map(this::toSeatDto).collect(Collectors.toList());
    }

    private ShowDto toDto(Show show) {
        ShowDto dto = new ShowDto();
        dto.setId(show.getId());
        dto.setMovieId(show.getMovie().getId());
        dto.setTheaterId(show.getTheater().getId());
        dto.setShowTime(show.getShowTime());
        dto.setPrice(show.getPrice());
        dto.setMovieTitle(show.getMovie().getTitle());
        dto.setTheaterName(show.getTheater().getName());
        return dto;
    }

    private SeatDto toSeatDto(Seat seat) {
        SeatDto dto = new SeatDto();
        dto.setId(seat.getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setShowId(seat.getShow().getId());
        dto.setStatus(seat.getStatus().name());
        return dto;
    }
}
