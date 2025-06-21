package com.shriva.personal_finance_manager_backend_java.service;

import com.shriva.personal_finance_manager_backend_java.model.Transaction;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction addTransaction(Transaction transaction) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> listTransactions() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .toList();
    }

    public Transaction editTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to edit this transaction");
        }
        transaction.setDate(transactionDetails.getDate());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setType(transactionDetails.getType());
        transaction.setCategory(transactionDetails.getCategory());
        transaction.setNotes(transactionDetails.getNotes());
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this transaction");
        }
        transactionRepository.delete(transaction);
    }
}