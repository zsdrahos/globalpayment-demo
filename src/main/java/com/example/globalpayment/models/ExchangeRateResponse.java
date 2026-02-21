package com.example.globalpayment.models;

public record ExchangeRateResponse(
    String from,
    String to,
    java.math.BigDecimal rate
) {}