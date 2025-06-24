package com.shriva.personal_finance_manager_backend_java.controller;

import com.shriva.personal_finance_manager_backend_java.model.Budget;
import com.shriva.personal_finance_manager_backend_java.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PutMapping("/edit/{id}")
    public ResponseEntity<Budget> editBudget(@PathVariable Long id, @RequestBody Budget budgetDetails) {
        Budget updatedBudget = budgetService.editBudget(id, budgetDetails);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Budget>> filterBudgets(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category) {
        List<Budget> budgets = budgetService.filterBudgets(startDate, endDate, category);
        return ResponseEntity.ok(budgets);
    }
}