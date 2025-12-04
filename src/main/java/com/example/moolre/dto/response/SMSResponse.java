package com.example.moolre.dto.response;

import lombok.Builder;

@Builder
public record SMSResponse(
        Integer status,
        String code,
        String message,
        Object data,
        String go
) {}
