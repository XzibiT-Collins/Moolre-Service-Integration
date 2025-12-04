package com.example.moolre.utils;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.request.SMSRequest;
import com.example.moolre.dto.response.SMSResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SMSIntegration {
    @Value("${moolre.messages.sender-id}")
    private String senderId;

    @Value("${moolre.api.vas-key}")
    private String vasKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public SMSResponse sendSMS(List<MessageRequest> msgRequest){
        try{
            SMSRequest smsRequest = SMSRequest
                    .builder()
                    .type(1)
                    .senderid(senderId)
                    .messages(msgRequest)
                    .build();

            return webClient
                    .post()
                    .uri("/open/sms/send")
                    .header("X-API-VASKEY", vasKey)
                    .bodyValue(smsRequest)
                    .retrieve()
                    .bodyToMono(String.class)  // Server returning Text/html instead of application/json
                    .map(responseBody -> {
                        log.info("Raw SMS API response: {}", responseBody);
                        try {
                            return objectMapper.readValue(responseBody, SMSResponse.class);
                        } catch (Exception e) {
                            log.error("Failed to parse SMS response: {}", e.getMessage());
                            return SMSResponse.builder()
                                    .status(500)
                                    .code("PARSE_ERROR")
                                    .message("Failed to parse response")
                                    .build();
                        }
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error sending SMS - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return SMSResponse.builder()
                    .status(e.getStatusCode().value())
                    .code("HTTP_ERROR")
                    .message(e.getResponseBodyAsString())
                    .build();
        } catch (Exception e){
            log.error("Error sending SMS: {}", e.getMessage(), e);
            return SMSResponse.builder()
                    .status(500)
                    .code("ERROR")
                    .message("Error sending SMS: " + e.getMessage())
                    .build();
        }
    }
}
