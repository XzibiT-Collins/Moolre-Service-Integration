package com.example.moolre.controller;

import com.example.moolre.dto.request.PayeeValidationRequest;
import com.example.moolre.services.interfaces.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/validate-payee")
    public ResponseEntity<String> validatePayee(@Valid @RequestBody PayeeValidationRequest request){
        return ResponseEntity.ok(paymentService.validatePayee(request));
    }
}
