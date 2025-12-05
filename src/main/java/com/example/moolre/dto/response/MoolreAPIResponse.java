package com.example.moolre.dto.response;

import lombok.Builder;

@Builder
public record MoolreAPIResponse(
        Integer status,
        String code,
        String message,
        Object data,
        String go
) {}
