package com.example.globalpayment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.globalpayment.enums.IdempotencyStatus;
import com.example.globalpayment.models.IdempotencyRecord;
import com.example.globalpayment.models.DTO.TransferRequest;
import com.example.globalpayment.models.DTO.TransferResponse;
import com.example.globalpayment.repository.IdempotencyRepository;
import com.example.globalpayment.services.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class PostController {

    private final TransferService transferService;
    private final IdempotencyRepository idempotencyRepository;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> createTransfer(
            @RequestHeader("X-Idempotency-Key") String key,
            @RequestBody TransferRequest request) {

        var existingRecord = idempotencyRepository.findById(key);

        if (existingRecord.isPresent()) {
            IdempotencyRecord record = existingRecord.get();
            return switch (record.getStatus()) {
                case PROCESSING -> ResponseEntity.status(HttpStatus.CONFLICT).body("Request already in progress");
                case SUCCESS -> ResponseEntity.ok(record.getResponseBody());
                case FAILED -> processNewRequest(key, request); 
            };
        }

        return processNewRequest(key, request);
    }

    private ResponseEntity<TransferResponse> processNewRequest(String key, TransferRequest request) {
        var record = IdempotencyRecord.builder()
                .key(key)
                .status(IdempotencyStatus.PROCESSING)
                .build();
        idempotencyRepository.save(record);

        try {
            TransferResponse response = transferService.executeTransfer(request);
            
            record.setStatus(IdempotencyStatus.SUCCESS);
            record.setResponseBody(objectMapper.writeValueAsString(response));
            idempotencyRepository.save(record);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            record.setStatus(IdempotencyStatus.FAILED);
            idempotencyRepository.save(record);
            throw new RuntimeException(e.getMessage());
        }
    }
}
