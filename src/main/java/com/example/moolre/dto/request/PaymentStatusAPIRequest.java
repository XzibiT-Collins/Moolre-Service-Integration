package com.example.moolre.dto.request;

import lombok.Builder;

@Builder
public record PaymentStatusAPIRequest(
        Integer type,
        Integer idtype,
        String id,
        String accountnumber
) {
}
