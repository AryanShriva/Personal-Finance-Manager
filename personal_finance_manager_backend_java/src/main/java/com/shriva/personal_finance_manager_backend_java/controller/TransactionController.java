package com.shriva.personal_finance_manager_backend_java.controller;

import com.shriva.personal_finance_manager_backend_java.model.Transaction;
import com.shriva.personal_finance_manager_backend_java.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Transaction>> listTransactions() {
        List<Transaction> transactions = transactionService.listTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Transaction> editTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.editTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> filterTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type) {
        List<Transaction> transactions = transactionService.filterTransactions(startDate, endDate, category, type);
        return ResponseEntity.ok(transactions);
    }
}