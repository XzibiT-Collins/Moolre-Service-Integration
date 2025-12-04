package com.example.moolre.dto.request;

public record Message(
        String recipient,
        String message,
        String ref
) {
}
