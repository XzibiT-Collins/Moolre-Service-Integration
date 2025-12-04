package com.example.moolre.dto.request;

import java.util.UUID;

public record MessageRequest(
        String recipient,
        String message,
        String ref
) {
    public MessageRequest(String recipient, String message) {
        this(recipient, message, "ref-" + UUID.randomUUID().toString().substring(0, 14));
    }
}
