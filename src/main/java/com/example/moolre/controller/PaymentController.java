package com.example.moolre.controller;

import com.example.moolre.dto.request.PayeeValidationRequest;
import com.example.moolre.dto.request.PaymentRequest;
import com.example.moolre.services.interfaces.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/validate-payee")
    public ResponseEntity<String> validatePayee(@Valid @RequestBody PayeeValidationRequest request){
        return ResponseEntity.ok(paymentService.validatePayee(request));
    }

    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePayment(@Valid @RequestBody PaymentRequest request){
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    @GetMapping("/status/{referenceId}")
    public ResponseEntity<String> getPaymentStatus(@PathVariable String referenceId){
        return ResponseEntity.ok(paymentService.getPaymentStatus(referenceId));
    }
}
