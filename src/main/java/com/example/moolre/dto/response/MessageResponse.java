package com.example.moolre.dto.response;

import lombok.Builder;

@Builder
public record MessageResponse(
        String message
) {}
