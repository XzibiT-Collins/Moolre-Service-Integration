package com.example.moolre.controller;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.response.MessageResponse;
import com.example.moolre.dto.response.MessageStatusResponse;
import com.example.moolre.enums.SMSSendType;
import com.example.moolre.model.Message;
import com.example.moolre.services.interfaces.SMSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SMSController {
    private final SMSService smsService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendSMS(@Valid @RequestBody MessageRequest smsRequest,@Valid @RequestParam(value = "sendType", defaultValue = "POST")SMSSendType sendType){
        return ResponseEntity.ok(smsService.sendSMS(smsRequest,sendType));
    }


    @GetMapping("/all")
    public ResponseEntity<Page<Message>> getAllMessages(){
        return ResponseEntity.status(HttpStatus.OK).body(smsService.getAllMessages());
    }

    @GetMapping("/get-status/{ref}")
    public ResponseEntity<String> getSMSStatus(@PathVariable String ref){
        return ResponseEntity.ok(smsService.getSMSStatus(ref));
    }
}
