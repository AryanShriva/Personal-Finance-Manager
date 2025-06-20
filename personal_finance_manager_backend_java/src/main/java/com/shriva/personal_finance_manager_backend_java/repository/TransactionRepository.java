package com.shriva.personal_finance_manager_backend_java.repository;

import com.shriva.personal_finance_manager_backend_java.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}