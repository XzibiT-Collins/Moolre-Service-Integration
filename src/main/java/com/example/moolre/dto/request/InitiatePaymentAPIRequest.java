package com.example.moolre.dto.request;

import com.example.moolre.enums.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record InitiatePaymentAPIRequest(
   Integer type,
   Integer channel,
   Currency currency,
   BigDecimal amount,
   String receiver,
   String sublistid,
   String externalref,
   String reference,
   String accountnumber
) {}
