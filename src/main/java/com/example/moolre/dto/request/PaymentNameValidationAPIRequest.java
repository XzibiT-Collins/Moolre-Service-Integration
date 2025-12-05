package com.example.moolre.dto.request;

import com.example.moolre.enums.Currency;
import lombok.Builder;

@Builder
public record PaymentNameValidationAPIRequest(
        Integer type,
        String receiver,
        Integer channel,
        String sublistid,
        Currency currency,
        String accountnumber
) {}
