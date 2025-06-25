package com.shriva.personal_finance_manager_backend_java.service;

import com.shriva.personal_finance_manager_backend_java.model.Transaction;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.shriva.personal_finance_manager_backend_java.exception.ResourceNotFoundException;
import com.shriva.personal_finance_manager_backend_java.exception.UnauthorizedException;

import java.time.LocalDate;
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
        return transactionRepository.findByUser(user);
    }

    public Transaction editTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        if (!transaction.getUser().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new UnauthorizedException("Unauthorized to edit this transaction");
        }
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setType(transactionDetails.getType());
        transaction.setDate(transactionDetails.getDate());
        transaction.setCategory(transactionDetails.getCategory());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setNotes(transactionDetails.getNotes());
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        if (!transaction.getUser().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new UnauthorizedException("Unauthorized to delete this transaction");
        }
        transactionRepository.delete(transaction);
    }

    public List<Transaction> filterTransactions(LocalDate startDate, LocalDate endDate, String category, String type) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionRepository.findByUserAndFilters(user, startDate, endDate, category, type);
    }
}