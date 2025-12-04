package com.example.moolre.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record SMSStatusRequest(
        Integer type,
        List<String> ref
) {
}
