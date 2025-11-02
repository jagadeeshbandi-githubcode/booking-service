package com.example.maersk;

import com.example.maersk.dto.BookingCreateRequest;
import com.example.maersk.model.BookingDocument;
import com.example.maersk.repository.BookingRepository;
import com.example.maersk.service.BookingService;
import com.example.maersk.service.SequenceGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;

public class BookingServiceTest {

    @Test
    void createBooking_savesAndReturnsRef() {
        BookingRepository repo = Mockito.mock(BookingRepository.class);
        SequenceGeneratorService seqSvc = Mockito.mock(SequenceGeneratorService.class);
        WebClient client = Mockito.mock(WebClient.class);

        BookingService service = new BookingService(client, repo, seqSvc);

        BookingCreateRequest req = new BookingCreateRequest(20, "DRY", "Southampton", "Singapore", 5, "2020-10-12T13:53:09Z");

        Mockito.when(seqSvc.generateSequence(Mockito.eq("bookingRef"), Mockito.anyLong()))
                .thenReturn(Mono.just(957000001L));
        Mockito.when(repo.save(any(BookingDocument.class))).thenReturn(Mono.just(new BookingDocument("957000001", 20, "DRY", "Southampton", "Singapore", 5, "2020-10-12T13:53:09Z")));

        service.createBooking(req).block();
    }
}
