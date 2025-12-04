package com.example.moolre.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MessageStatusResponse(
        Integer status,
        String code,
        String message,
        List<Data> data,
        String go
) {
}
