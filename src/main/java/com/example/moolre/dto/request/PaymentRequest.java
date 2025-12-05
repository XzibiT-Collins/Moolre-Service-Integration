package com.example.moolre.dto.request;

import com.example.moolre.enums.Channel;
import com.example.moolre.validators.annotations.ValidReceiver;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest (
        @NotBlank
        @ValidReceiver
        String receiver,
        @Min(value = 1, message = "Amount must be greater than zero")
        BigDecimal amount,
        @NotNull(message = "Channel cannot be blank")
        Channel channel,
        String reference,
        String sublistid
) {}
