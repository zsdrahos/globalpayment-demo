package com.example.globalpayment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.globalpayment.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
