package com.catalogo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(
        int status,
        String message,
        LocalDateTime timestamp,
        List<String> errors
) {
    public ErrorResponseDTO(int status, String message) {
        this(status, message, LocalDateTime.now(), List.of());
    }

    public ErrorResponseDTO(int status, String message, List<String> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
