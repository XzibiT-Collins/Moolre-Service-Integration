package com.example.moolre.services.impl;

import com.example.moolre.dto.request.PayeeValidationRequest;
import com.example.moolre.dto.request.PaymentRequest;
import com.example.moolre.dto.response.MoolreAPIResponse;
import com.example.moolre.dto.response.PaymentDataResponse;
import com.example.moolre.dto.response.TransactionStatusData;
import com.example.moolre.repository.TransactionRepository;
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
    private final TransactionRepository transactionRepository;

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

    @Override
    public String getPaymentStatus(String ref) {
        if(ref == null || ref.isBlank()) return "Invalid reference";
        if(!transactionRepository.existsByExternalref(ref)){
            return "Transaction not found";
        }

        MoolreAPIResponse response = paymentIntegration.getPaymentStatus(ref);

        if(response.status() == 1){
            try{
                TransactionStatusData transactionStatusData = objectMapper.readValue(response.data().toString(),TransactionStatusData.class);

                switch (transactionStatusData.txstatus()){
                    case 0 -> {
                        return "Transaction pending";
                    }
                    case 1 -> {
                        return "Transaction successful";
                    }
                    case 2 -> {
                        return "Transaction failed";
                    }
                    case 3 -> {
                        return "Transaction status unknown";
                    }
                }
            }catch (Exception e){
                log.error("Failed to parse transaction status response: {}", e.getMessage());
            }
        }
        return response.message();
    }
}
