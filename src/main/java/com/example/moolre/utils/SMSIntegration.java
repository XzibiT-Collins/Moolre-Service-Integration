package com.example.moolre.utils;

import com.example.moolre.dto.request.MessageRequest;
import com.example.moolre.dto.request.SMSRequest;
import com.example.moolre.dto.request.SMSStatusRequest;
import com.example.moolre.dto.response.MessageStatusResponse;
import com.example.moolre.dto.response.MoolreAPIResponse;
import com.example.moolre.exception.BadRequestException;
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

    public MoolreAPIResponse sendSMS(List<MessageRequest> msgRequest){
        try{
            SMSRequest smsRequest = SMSRequest
                    .builder()
                    .type(1)
                    .senderid(senderId)
                    .messages(msgRequest)
                    .build();

            MoolreAPIResponse response = webClient
                    .post()
                    .uri("/open/sms/send")
                    .header("X-API-VASKEY", vasKey)
                    .bodyValue(smsRequest)
                    .retrieve()
                    .bodyToMono(String.class)  // Server returning Text/html instead of application/json
                    .map(responseBody -> {
                        log.info("Raw SMS API response: {}", responseBody);
                        try {
                            return objectMapper.readValue(responseBody, MoolreAPIResponse.class);
                        } catch (Exception e) {
                            log.error("Failed to parse SMS response: {}", e.getMessage());
                            throw new BadRequestException("Failed to parse SMS response");
                        }
                    })
                    .block();
            return handleApiStatusError(response);
        } catch (WebClientResponseException e) {
            log.error("Error sending SMS - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BadRequestException(e.getResponseBodyAsString());
        } catch (Exception e){
            log.error("Error sending SMS: {}", e.getMessage(), e);
            throw e;
        }
    }

    public MessageStatusResponse getSMSStatus(List<String> refs){
        SMSStatusRequest request = SMSStatusRequest
                .builder()
                .type(5)
                .ref(refs)
                .build();
        try{
            String contentType = webClient
                    .post()
                    .uri("/open/sms/query")
                    .retrieve()
                    .toBodilessEntity()
                    .block()
                    .getHeaders()
                    .getContentType()
                    .toString();

            log.info("API ContentType: {}", contentType);
            return webClient
                    .post()
                    .uri("/open/sms/query")
                    .header("X-API-VASKEY", vasKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(responseBody->{
                        log.info("Raw API response: {}", responseBody);
                        try{
                            return objectMapper.readValue(responseBody, MessageStatusResponse.class);
                        }catch (Exception e){
                            log.error("Failed to parse SMS status response: {}", e.getMessage());
                            throw new BadRequestException("Failed to parse SMS status response");
                        }
                    }).block();
        }catch (WebClientResponseException e){
            log.error("Error: ",e);
            log.error("Error getting message status - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BadRequestException(e.getResponseBodyAsString());
        }catch (Exception e){
            log.info("Error getting message status: {}", e.getMessage(), e);
            throw e;
        }
    }

    public MoolreAPIResponse sendSMSViaQueryParams(MessageRequest request){
        try{
            MoolreAPIResponse response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/open/sms/send")
                            .queryParam("type", 1)
                            .queryParam("senderid", senderId)
                            .queryParam("message", request.message())
                            .queryParam("recipient", request.recipient())
                            .queryParam("X-API-VASKEY", vasKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(responseBody->{
                        log.info("Raw SMS API response: {}", responseBody);
                        try{
                            return objectMapper.readValue(responseBody, MoolreAPIResponse.class);
                        }catch (Exception e){
                            log.error("Failed to parse SMS response: {}", e.getMessage());
                            throw new BadRequestException("Failed to parse SMS response");
                        }
                    })
                    .block();
            return handleApiStatusError(response);

        }catch(WebClientResponseException e){
            log.error("Error sending SMS: {}", e.getMessage(), e);
            throw new BadRequestException("Error sending SMS");
        }catch (Exception e){
            log.error("Error sending SMS: {}", e.getMessage(), e);
            throw e;
        }
    }

    protected MoolreAPIResponse handleApiStatusError(MoolreAPIResponse response){
        try{
            if(response.status()== 1){
                return response;
            }else{
                throw new BadRequestException(response.message());
            }
        }catch (Exception e){
            log.error("Error occurred while processing response: {}", e.getMessage(), e);
            throw e;
        }
    }
}
