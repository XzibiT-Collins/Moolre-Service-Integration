package com.example.moolre.services.interfaces;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.response.MessageResponse;
import com.example.moolre.enums.SMSSendType;
import com.example.moolre.model.Message;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SMSService {
    String sendSMS(MessageRequest smsRequest, SMSSendType sendType);
    MessageResponse sendBulkSMS(List<MessageRequest> smsRequest);
    String getSMSStatus(String ref);
    Page<Message> getAllMessages();
}
