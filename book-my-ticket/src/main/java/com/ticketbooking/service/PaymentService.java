package com.ticketbooking.service;

import com.ticketbooking.dto.PaymentDto;
import com.ticketbooking.exception.PaymentFailedException;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.model.Booking;
import com.ticketbooking.model.Payment;
import com.ticketbooking.model.enums.BookingStatus;
import com.ticketbooking.model.enums.PaymentStatus;
import com.ticketbooking.repository.BookingRepository;
import com.ticketbooking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final Random random = new Random();

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public PaymentDto processPayment(PaymentDto dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + dto.getBookingId()));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new PaymentFailedException("Booking is already confirmed");
        }

        // Mock payment: 80% success rate
        boolean success = random.nextInt(100) < 80;

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMode(dto.getPaymentMode());

        if (success) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);
        }

        bookingRepository.save(booking);
        payment = paymentRepository.save(payment);

        if (!success) {
            throw new PaymentFailedException("Payment failed. Booking has been cancelled.");
        }

        return toDto(payment);
    }

    private PaymentDto toDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setBookingId(payment.getBooking().getId());
        dto.setPaymentMode(payment.getPaymentMode());
        dto.setPaymentStatus(payment.getPaymentStatus().name());
        return dto;
    }
}
