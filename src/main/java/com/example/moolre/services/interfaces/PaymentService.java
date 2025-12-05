package com.example.moolre.services.interfaces;

import com.example.moolre.dto.request.PayeeValidationRequest;

public interface PaymentService {
    String validatePayee(PayeeValidationRequest request);
}
