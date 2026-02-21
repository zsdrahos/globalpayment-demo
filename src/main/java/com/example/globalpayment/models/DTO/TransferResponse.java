package com.example.globalpayment.models.DTO;

public record TransferResponse(
    Long transactionId,
    String status,
    String message
) {}
