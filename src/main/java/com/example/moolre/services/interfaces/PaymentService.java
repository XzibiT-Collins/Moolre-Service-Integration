package com.example.moolre.services.interfaces;

import com.example.moolre.dto.request.PayeeValidationRequest;
import com.example.moolre.dto.request.PaymentRequest;

public interface PaymentService {
    String validatePayee(PayeeValidationRequest request);
    String initiatePayment(PaymentRequest request);
}
