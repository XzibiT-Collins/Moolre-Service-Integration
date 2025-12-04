package com.example.moolre.dto.request;

import lombok.Builder;
import java.util.List;

@Builder
public record SMSRequest(
    Integer type,
    String senderid,
    List<MessageRequest> messages
) {}
