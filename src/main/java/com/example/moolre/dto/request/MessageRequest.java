package com.example.moolre.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record MessageRequest(
        String recipient,
        String message,
        String ref
) {
    @JsonCreator
    public MessageRequest(
            @JsonProperty("recipient") String recipient,
            @JsonProperty("message") String message,
            @JsonProperty("ref") String ref) {
        this.recipient = recipient;
        this.message = message;
        this.ref = (ref == null || ref.isBlank()) ? "ref-" + UUID.randomUUID().toString().substring(0, 13) : ref;
    }

    public MessageRequest(String recipient, String message) {
        this(recipient, message, null);
    }
}
