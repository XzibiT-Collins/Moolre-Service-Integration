package com.example.moolre.services.impl;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.response.MessageResponse;
import com.example.moolre.dto.response.SMSResponse;
import com.example.moolre.services.interfaces.SMSService;
import com.example.moolre.utils.SMSIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {

    private final SMSIntegration smsIntegration;

    @Override
    public MessageResponse sendSMS(MessageRequest smsRequest) {
        log.info("Sending SMS: {}", smsRequest);
        SMSResponse response = smsIntegration.sendSMS(List.of(smsRequest));
        return MessageResponse.builder()
                .message(response.message())
                .build();
    }

    @Override
    public MessageResponse sendBulkSMS(List<MessageRequest> smsRequest) {
        return null;
    }
}
