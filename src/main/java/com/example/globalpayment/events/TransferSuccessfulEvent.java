package com.example.globalpayment.events;

import java.math.BigDecimal;

public record TransferSuccessfulEvent(
    Long sourceAccountId,
    Long targetAccountId,
    BigDecimal finalAmount,
    String currency,
    long timestamp
) {}