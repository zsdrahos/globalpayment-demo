package com.example.globalpayment.services;

import java.math.BigDecimal;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.globalpayment.clients.ExchangeRateClient;
import com.example.globalpayment.events.TransferSuccessfulEvent;
import com.example.globalpayment.models.Account;
import com.example.globalpayment.models.DTO.TransferRequest;
import com.example.globalpayment.models.DTO.TransferResponse;
import com.example.globalpayment.repository.AccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final ExchangeRateClient exchangeRateClient;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public TransferResponse executeTransfer(TransferRequest request) {
        log.info("Starting transfer: {} {} from account {} to {}", 
                 request.amount(), request.currency(), request.fromAccountId(), request.toAccountId());

        Account source = accountRepository.findById(request.fromAccountId())
            .orElseThrow(() -> new RuntimeException("Source account not found"));
        
        Account target = accountRepository.findById(request.toAccountId())
            .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (source.getBalance().compareTo(request.amount()) < 0) {
            log.error("Transfer failed: Insufficient funds in account {}", source.getId());
            throw new RuntimeException("Insufficient funds");
        }

        BigDecimal finalAmount = request.amount();
        if (!source.getCurrency().equals(target.getCurrency())) {
            log.info("Currency mismatch. Fetching exchange rate for {} -> {}", 
                     source.getCurrency(), target.getCurrency());
            
            var rateResponse = exchangeRateClient.getRate(source.getCurrency(), target.getCurrency());
            finalAmount = request.amount().multiply(rateResponse.rate());
            
            log.info("Exchange rate applied: {}. Final amount: {}", rateResponse.rate(), finalAmount);
        }

        source.setBalance(source.getBalance().subtract(request.amount()));
        target.setBalance(target.getBalance().add(finalAmount));

        accountRepository.save(source);
        accountRepository.save(target);

        eventPublisher.publishEvent(new TransferSuccessfulEvent(
            source.getId(),
            target.getId(),
            finalAmount,
            target.getCurrency(),
            System.currentTimeMillis()
        ));

        log.info("Transfer successfully completed for request: {}", request);
        
        return new TransferResponse(System.currentTimeMillis(), "SUCCESS", "Transfer completed");
    }
}