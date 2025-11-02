package com.example.maersk.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NotNull
    @Min(20)
    @Max(40)
    private Integer containerSize;

    @NotNull
    @Pattern(regexp = "DRY|REEFER")
    private String containerType;

    @NotBlank
    @Size(min = 5, max = 20)
    private String origin;

    @NotBlank
    @Size(min = 5, max = 20)
    private String destination;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer quantity;
}
