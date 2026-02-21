package com.example.globalpayment.models.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
    @NotNull Long fromAccountId,
    @NotNull Long toAccountId,
    @Positive @NotNull BigDecimal amount,
    @NotBlank String currency
) {}
