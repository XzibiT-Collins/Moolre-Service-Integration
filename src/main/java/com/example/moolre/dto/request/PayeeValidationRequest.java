package com.example.moolre.dto.request;

import com.example.moolre.enums.Channel;
import com.example.moolre.validators.annotations.ValidReceiver;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PayeeValidationRequest(
        @NotBlank(message = "Receiver cannot be blank")
        @ValidReceiver
        String receiver,
        @NotNull(message = "Channel cannot be blank")
        Channel channel,
        String sublistid
) {
}
