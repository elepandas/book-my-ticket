package com.ticketbooking.dto;

public class SeatDto {

    private Long id;
    private String seatNumber;
    private Long showId;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
