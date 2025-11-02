package com.example.maersk.service;

import com.example.maersk.dto.*;
import com.example.maersk.model.BookingDocument;
import com.example.maersk.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

@Service
public class BookingService {
    private final WebClient webClient;
    private final BookingRepository bookingRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private static final long START_REF = 957000000L;

    public BookingService(WebClient webClient,
                          BookingRepository bookingRepository,
                          SequenceGeneratorService sequenceGeneratorService) {
        this.webClient = webClient;
        this.bookingRepository = bookingRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public Mono<AvailabilityResponse> checkAvailability(BookingRequest request) {
        return webClient.post()
                .uri("https://maersk.com/api/bookings/checkAvailable")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ExternalAvailabilityResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(200)).filter(throwable -> true))
                .map(ext -> new AvailabilityResponse(ext.getAvailableSpace() != null && ext.getAvailableSpace() > 0));
    }

    public Mono<BookingCreateResponse> createBooking(BookingCreateRequest request) {
        return sequenceGeneratorService.generateSequence("bookingRef", START_REF)
                .flatMap(seq -> {
                    long next = seq;
                    String bookingRef = String.format("%09d", next);
                    BookingDocument doc = new BookingDocument(
                            bookingRef,
                            request.getContainerSize(),
                            request.getContainerType(),
                            request.getOrigin(),
                            request.getDestination(),
                            request.getQuantity(),
                            request.getTimestamp()
                    );
                    return bookingRepository.save(doc)
                            .map(saved -> new BookingCreateResponse(bookingRef))
                            .onErrorMap(e -> new RuntimeException("DB_SAVE_ERROR", e));
                });
    }

    public static record BookingCreateResponse(String bookingRef) {}
}
