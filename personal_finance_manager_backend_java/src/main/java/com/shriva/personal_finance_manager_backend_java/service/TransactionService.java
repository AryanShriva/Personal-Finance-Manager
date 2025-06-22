package com.shriva.personal_finance_manager_backend_java.service;

import com.shriva.personal_finance_manager_backend_java.model.Transaction;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Predefined categories
    private final List<String> predefinedCategories = new ArrayList<>(List.of(
            "Groceries", "Utilities", "Transportation", "Entertainment", "Salary", "Other"
    ));

    public Transaction addTransaction(Transaction transaction) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transaction.setUser(user);
        // Validate category if provided
        if (transaction.getCategory() != null && !predefinedCategories.contains(transaction.getCategory().getName()) && transaction.getCategory().getId() == null) {
            throw new RuntimeException("Invalid category. Use predefined categories or create a custom one.");
        }
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
        // Validate category if updated
        if (transactionDetails.getCategory() != null && !predefinedCategories.contains(transactionDetails.getCategory().getName()) && transactionDetails.getCategory().getId() == null) {
            throw new RuntimeException("Invalid category. Use predefined categories or create a custom one.");
        }
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

    public List<Transaction> filterTransactions(LocalDate startDate, LocalDate endDate, String category, String type) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .toList();

        if (startDate != null) {
            transactions = transactions.stream().filter(t -> t.getDate().isAfter(startDate) || t.getDate().isEqual(startDate)).toList();
        }
        if (endDate != null) {
            transactions = transactions.stream().filter(t -> t.getDate().isBefore(endDate) || t.getDate().isEqual(endDate)).toList();
        }
        if (category != null && !category.isEmpty()) {
            transactions = transactions.stream().filter(t -> t.getCategory() != null && category.equals(t.getCategory().getName())).toList();
        }
        if (type != null && !type.isEmpty()) {
            transactions = transactions.stream().filter(t -> type.equals(t.getType())).toList();
        }

        return transactions;
    }
}