package com.shriva.personal_finance_manager_backend_java.controller;

import com.shriva.personal_finance_manager_backend_java.model.Budget;
import com.shriva.personal_finance_manager_backend_java.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/add")
    public ResponseEntity<Budget> addBudget(@RequestBody Budget budget) {
        Budget savedBudget = budgetService.addBudget(budget);
        return ResponseEntity.ok(savedBudget);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Budget>> listBudgets() {
        List<Budget> budgets = budgetService.listBudgets();
        return ResponseEntity.ok(budgets);
    }
}