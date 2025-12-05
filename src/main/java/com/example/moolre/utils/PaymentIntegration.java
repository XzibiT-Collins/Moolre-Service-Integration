package com.example.moolre.utils;

import com.example.moolre.dto.request.*;
import com.example.moolre.dto.response.MoolreAPIResponse;
import com.example.moolre.enums.Channel;
import com.example.moolre.enums.Currency;
import com.example.moolre.exception.BadRequestException;
import com.example.moolre.model.Transaction;
import com.example.moolre.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentIntegration {

    @Value("${moolre.api.secret.key}")
    private String secretKey;

    @Value("${moolre.api.public.key}")
    private String publicKey;

    @Value("${moolre.account-number}")
    private String accountNumber;

    @Value("${moolre.api.user}")
    private String moolreUsername;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final TransactionRepository transactionRepository;

    public MoolreAPIResponse validatePayee(PayeeValidationRequest request){
        log.info("Validating payee: {}", request);
        Integer channel = switch (request.channel()) {
            case Channel.MTN -> 1;
            case Channel.TELECEL -> 6;
            case Channel.AIRTEL_TIGO -> 7;
            case Channel.BANK -> 2;
        };
        PaymentNameValidationAPIRequest body = PaymentNameValidationAPIRequest
                .builder()
                .type(1)
                .receiver(request.receiver())
                .channel(channel)
                .sublistid(request.sublistid())
                .currency(Currency.GHS)
                .accountnumber(accountNumber)
                .build();
        try{
            return webClient
                    .post()
                    .uri("/open/transact/validate")
                    .header("X-API-USER", moolreUsername)
                    .header("X-API-KEY", secretKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(responseBody->{
                        log.info("Raw API response: {}", responseBody);
                        try{
                            return objectMapper.readValue(responseBody,MoolreAPIResponse.class);
                        }catch (Exception e){
                            throw new BadRequestException("Error parsing response");
                        }
                    }).block();
        }catch (WebClientResponseException e){
            log.error("Error validating payee: {}", e.getMessage(), e);
            throw new BadRequestException("Error validating payee");
        }catch (Exception e){
            log.error("Error validating payee: {}", e.getMessage(), e);
            throw e;
        }
    }

    public MoolreAPIResponse initiatePayment(PaymentRequest request){
        log.info("Initiating payment: {}", request);
        Integer channel = switch (request.channel()) {
            case Channel.MTN -> 1;
            case Channel.TELECEL -> 6;
            case Channel.AIRTEL_TIGO -> 7;
            case Channel.BANK -> 2;
        };

        InitiatePaymentAPIRequest body = InitiatePaymentAPIRequest
                .builder()
                .type(1)
                .channel(channel)
                .currency(Currency.GHS)
                .amount(request.amount())
                .receiver(request.receiver())
                .sublistid(request.sublistid())
                .externalref("ref-"+ UUID.randomUUID().toString().substring(0,13))
                .reference(request.reference())
                .accountnumber(accountNumber)
                .build();

        saveTransaction(body);

        try{
            return webClient
                    .post()
                    .uri("/open/transact/transfer")
                    .header("X-API-KEY", secretKey)
                    .header("X-API-USER", moolreUsername)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(responseBody->{
                        log.info("Raw API response: {}", responseBody);
                        try{
                            return objectMapper.readValue(responseBody,MoolreAPIResponse.class);
                        }catch (Exception e){
                            throw new BadRequestException("Error parsing response");
                        }
                    }).block();
        }catch (WebClientResponseException e){
            log.error("Error initiating payment: {}", e.getMessage(), e);
            throw new BadRequestException("Error initiating payment");
        }catch (Exception e){
            log.error("Error initiating payment: {}", e.getMessage(), e);
            throw e;
        }
    }

    public MoolreAPIResponse getPaymentStatus(String reference){
        PaymentStatusAPIRequest body = PaymentStatusAPIRequest
                .builder()
                .type(1)
                .idtype(1)
                .id(reference)
                .accountnumber(accountNumber)
                .build();

        try{
            return webClient
                    .post()
                    .uri("/open/transact/status")
                    .header("X-API-USER", moolreUsername)
                    .header("X-API-KEY", secretKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(responseBody->{
                        log.info("Raw API response: {}", responseBody);
                        try{
                            return objectMapper.readValue(responseBody,MoolreAPIResponse.class);
                        }catch (Exception e){
                            throw new BadRequestException("Error parsing response");
                        }
                    }).block();
        }catch (WebClientResponseException e){
            log.error("Error getting payment status: {}", e.getMessage(), e);
            throw new BadRequestException("Error getting payment status");
        }catch (Exception e){
            log.error("Error getting payment status: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void saveTransaction(InitiatePaymentAPIRequest body) {
        Transaction transaction = Transaction
                .builder()
                .type(body.type())
                .channel(body.channel())
                .currency(body.currency())
                .amount(body.amount())
                .receiver(body.receiver())
                .reference(body.reference())
                .sublistid(body.sublistid())
                .externalref(body.externalref())
                .accountnumber(body.accountnumber())
                .build();
        transactionRepository.save(transaction);
    }
}
