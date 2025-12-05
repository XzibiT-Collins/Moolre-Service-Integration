package com.example.moolre.dto.response;

public record PaymentDataResponse (
        Integer txstatus,
        String receiver,
        String transactionid,
        String externalref,
        String thirdpartyref,
        String receivername,
        String amount,
        String amountfee,
        String networkfee,
        String fee
){
}
