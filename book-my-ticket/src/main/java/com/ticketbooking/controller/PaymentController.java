package com.ticketbooking.controller;

import com.ticketbooking.dto.PaymentDto;
import com.ticketbooking.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDto> processPayment(@Valid @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(paymentDto));
    }
}
