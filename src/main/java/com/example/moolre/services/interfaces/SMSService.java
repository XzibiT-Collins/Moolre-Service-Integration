package com.example.moolre.services.interfaces;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.response.MessageResponse;

import java.util.List;

public interface SMSService {
    MessageResponse sendSMS(MessageRequest smsRequest);
    MessageResponse sendBulkSMS(List<MessageRequest> smsRequest);
}
