package com.example.maersk.controller;

import com.example.maersk.dto.*;
import com.example.maersk.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(path = "/check-availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AvailabilityResponse>> checkAvailability(@Valid @RequestBody BookingRequest request) {
        return bookingService.checkAvailability(request)
                .map(resp -> ResponseEntity.ok(resp));
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        return bookingService.createBooking(request)
                .map(resp -> ResponseEntity.ok().body(java.util.Map.of("bookingRef", resp.bookingRef())));
    }
}
