package com.example.moolre.controller;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.response.MessageResponse;
import com.example.moolre.services.interfaces.SMSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SMSController {
    private final SMSService smsService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendSMS(@Valid @RequestBody MessageRequest smsRequest){
        return ResponseEntity.ok(smsService.sendSMS(smsRequest));
    }
}
