package com.example.moolre.services.impl;

import com.example.moolre.dto.request.PayeeValidationRequest;
import com.example.moolre.dto.request.PaymentRequest;
import com.example.moolre.dto.response.MoolreAPIResponse;
import com.example.moolre.dto.response.PaymentDataResponse;
import com.example.moolre.services.interfaces.PaymentService;
import com.example.moolre.utils.PaymentIntegration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentIntegration paymentIntegration;
    private final ObjectMapper objectMapper;

    @Override
    public String validatePayee(PayeeValidationRequest request) {
        MoolreAPIResponse response = paymentIntegration.validatePayee(request);
        return response.data().toString();
    }

    @Override
    public String initiatePayment(PaymentRequest request) {
        MoolreAPIResponse response = paymentIntegration.initiatePayment(request);
        if(response.status() == 1){
            try{
                PaymentDataResponse paymentDataResponse = objectMapper.readValue(response.data().toString(),PaymentDataResponse.class);
                log.info("Payment initiated successfully: {}", paymentDataResponse);

            }catch (Exception e){
                log.error("Failed to parse payment data response: {}", e.getMessage());
            }
        }

        switch (response.status()){
            case 1 -> {
                return response.message();
            }
            case 0 -> {
                return "Payment failed: " + response.message();
            }
        }
        return response.message();
    }
}
