package com.example.moolre.services.impl;

import com.example.moolre.dto.request.PayeeValidationRequest;
import com.example.moolre.dto.response.MoolreAPIResponse;
import com.example.moolre.services.interfaces.PaymentService;
import com.example.moolre.utils.PaymentIntegration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentIntegration paymentIntegration;

    @Override
    public String validatePayee(PayeeValidationRequest request) {
        MoolreAPIResponse response = paymentIntegration.validatePayee(request);
        return response.data().toString();
    }
}
