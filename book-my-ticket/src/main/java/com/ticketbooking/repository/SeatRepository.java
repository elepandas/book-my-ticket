package com.ticketbooking.repository;

import com.ticketbooking.model.Seat;
import com.ticketbooking.model.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowId(Long showId);
    List<Seat> findByShowIdAndStatus(Long showId, SeatStatus status);
}
