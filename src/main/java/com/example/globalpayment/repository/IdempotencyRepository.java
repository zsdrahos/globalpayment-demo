package com.example.globalpayment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.globalpayment.models.IdempotencyRecord;

public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord, String> {
}