package com.ticketbooking.service;

import com.ticketbooking.dto.BookingDto;
import com.ticketbooking.exception.InvalidBookingException;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.exception.SeatAlreadyBookedException;
import com.ticketbooking.model.Booking;
import com.ticketbooking.model.Seat;
import com.ticketbooking.model.Show;
import com.ticketbooking.model.User;
import com.ticketbooking.model.enums.BookingStatus;
import com.ticketbooking.model.enums.SeatStatus;
import com.ticketbooking.repository.BookingRepository;
import com.ticketbooking.repository.SeatRepository;
import com.ticketbooking.repository.ShowRepository;
import com.ticketbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository,
                          ShowRepository showRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public BookingDto createBooking(BookingDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        Show show = showRepository.findById(dto.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + dto.getShowId()));

        List<Seat> seats = seatRepository.findAllById(dto.getSeatIds());
        if (seats.size() != dto.getSeatIds().size()) {
            throw new InvalidBookingException("One or more seats not found");
        }

        for (Seat seat : seats) {
            if (seat.getStatus() == SeatStatus.BOOKED) {
                throw new SeatAlreadyBookedException("Seat " + seat.getSeatNumber() + " is already booked");
            }
            if (!seat.getShow().getId().equals(show.getId())) {
                throw new InvalidBookingException("Seat " + seat.getSeatNumber() + " does not belong to the selected show");
            }
            seat.setStatus(SeatStatus.BOOKED);
        }
        seatRepository.saveAll(seats);

        BigDecimal totalAmount = show.getPrice().multiply(BigDecimal.valueOf(seats.size()));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setSeats(seats);
        booking.setTotalAmount(totalAmount);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        return toDto(booking);
    }

    public List<BookingDto> getBookingsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return bookingRepository.findByUserId(userId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setShowId(booking.getShow().getId());
        dto.setSeatIds(booking.getSeats().stream().map(Seat::getId).collect(Collectors.toList()));
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setBookingTime(booking.getBookingTime());
        dto.setStatus(booking.getStatus().name());
        return dto;
    }
}
