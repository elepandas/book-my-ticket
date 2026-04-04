package com.ticketbooking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PaymentDto {

    private Long id;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotBlank(message = "Payment mode is required")
    private String paymentMode;

    private String paymentStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
