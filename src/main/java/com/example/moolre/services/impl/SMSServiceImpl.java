package com.example.moolre.services.impl;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.response.MessageResponse;
import com.example.moolre.dto.response.MessageStatusResponse;
import com.example.moolre.dto.response.MoolreAPIResponse;
import com.example.moolre.enums.SMSSendType;
import com.example.moolre.exception.BadRequestException;
import com.example.moolre.model.Message;
import com.example.moolre.repository.MessageRepository;
import com.example.moolre.services.interfaces.SMSService;
import com.example.moolre.utils.SMSIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {

    private final SMSIntegration smsIntegration;
    private final MessageRepository messageRepository;

    @Override
    public String sendSMS(MessageRequest smsRequest, SMSSendType sendType) {
        log.info("Sending SMS: {}", smsRequest);
        log.info("Saving message to DB with ref {}", smsRequest.ref());
        Message message = Message
                .builder()
                .recipient(smsRequest.recipient())
                .messageBody(smsRequest.message())
                .ref(smsRequest.ref())
                .build();

        MoolreAPIResponse response = null;
        if(sendType.equals(SMSSendType.POST)){
            log.info("Sending SMS via POST");
            response = smsIntegration.sendSMS(List.of(smsRequest));
        }else if(sendType.equals(SMSSendType.GET)){
            if(smsRequest.message().length() > 160){
                throw new BadRequestException("Message length must not exceed 160 characters");
            }
            log.info("Sending SMS via query params");
            response = smsIntegration.sendSMSViaQueryParams(smsRequest);
        }else{
            throw new BadRequestException("Invalid SMS send type");
        }

        messageRepository.save(message);

        return response.message();
    }

    @Override
    public MessageResponse sendBulkSMS(List<MessageRequest> smsRequest) {
        return null;
    }

    @Override
    public String getSMSStatus(String ref) {
        MessageStatusResponse response = smsIntegration.getSMSStatus(List.of(ref));
        if(response.data().isEmpty()) return "SMS not found";
        String status = Optional.ofNullable(response.data().getFirst().status()).orElse("0");

        return switch (status) {
            case "0" -> "UNKNOWN";
            case "1" -> "SENT";
            case "2" -> "DELIVERED";
            case "3" -> "FAILED";
            default -> "PENDING";
        };
    }

    @Override
    public Page<Message> getAllMessages() {
        Pageable pageable = PageRequest.of(0,50);
        return messageRepository.findAll(pageable);
    }
}
