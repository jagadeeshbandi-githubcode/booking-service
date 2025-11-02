package com.example.maersk.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class BookingDocument {
    @Id
    @Field("booking_ref")
    private String bookingRef;

    @Field("container_size")
    private Integer containerSize;

    @Field("container_type")
    private String containerType;

    @Field("origin")
    private String origin;

    @Field("destination")
    private String destination;

    @Field("quantity")
    private Integer quantity;

    @Field("timestamp")
    private String timestamp;
}
