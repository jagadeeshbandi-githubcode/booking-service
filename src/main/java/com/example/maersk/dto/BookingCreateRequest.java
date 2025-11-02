package com.example.maersk.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest extends BookingRequest {
    @NotBlank
    private String timestamp;
}
