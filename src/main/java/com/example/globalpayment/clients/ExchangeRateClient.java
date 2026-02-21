package com.example.globalpayment.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.globalpayment.models.ExchangeRateResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "exchange-service", url = "${external.api.url}")
public interface ExchangeRateClient {

    @GetMapping("/api/rates")
    @Retry(name = "exchangeRateApi")
    @CircuitBreaker(name = "exchangeRateApi")
    ExchangeRateResponse getRate(
        @RequestParam("from") String from, 
        @RequestParam("to") String to
    );
}