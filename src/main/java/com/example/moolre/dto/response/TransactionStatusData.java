package com.example.moolre.dto.response;

public record TransactionStatusData(
        Integer txstatus,
        String transactionid,
        String externalref,
        String ts
){
}
