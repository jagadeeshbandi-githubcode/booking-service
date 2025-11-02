package com.example.maersk;

import com.example.maersk.controller.BookingController;
import com.example.maersk.dto.BookingRequest;
import com.example.maersk.dto.BookingCreateRequest;
import com.example.maersk.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import com.example.maersk.dto.AvailabilityResponse;

@WebFluxTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookingService bookingService;

    @Test
    void checkAvailability_returnsAvailable() {
        BookingRequest req = new BookingRequest(20, "DRY", "Southampton", "Singapore", 5);
        Mockito.when(bookingService.checkAvailability(Mockito.any()))
                .thenReturn(Mono.just(new AvailabilityResponse(true)));

        webTestClient.post().uri("/api/bookings/check-availability")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.available").isEqualTo(true);
    }

    @Test
    void createBooking_returnsBookingRef() {
        BookingCreateRequest req = new BookingCreateRequest(20, "DRY", "Southampton", "Singapore", 5, "2020-10-12T13:53:09Z");
        Mockito.when(bookingService.createBooking(Mockito.any()))
                .thenReturn(Mono.just(new com.example.maersk.service.BookingService.BookingCreateResponse("957000001")));

        webTestClient.post().uri("/api/bookings/create")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.bookingRef").isEqualTo("957000001");
    }
}
