package com.ticketbooking.service;

import com.ticketbooking.dto.MovieDto;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.model.Movie;
import com.ticketbooking.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return toDto(movie);
    }

    public MovieDto createMovie(MovieDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDuration(dto.getDuration());
        movie.setLanguage(dto.getLanguage());
        return toDto(movieRepository.save(movie));
    }

    public MovieDto updateMovie(Long id, MovieDto dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDuration(dto.getDuration());
        movie.setLanguage(dto.getLanguage());
        return toDto(movieRepository.save(movie));
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    private MovieDto toDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setDuration(movie.getDuration());
        dto.setLanguage(movie.getLanguage());
        return dto;
    }
}
