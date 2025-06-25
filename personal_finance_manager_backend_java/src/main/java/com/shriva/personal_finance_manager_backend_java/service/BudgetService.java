package com.shriva.personal_finance_manager_backend_java.service;

import com.shriva.personal_finance_manager_backend_java.model.Budget;
import com.shriva.personal_finance_manager_backend_java.model.Transaction;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.repository.BudgetRepository;
import com.shriva.personal_finance_manager_backend_java.repository.TransactionRepository;
import com.shriva.personal_finance_manager_backend_java.exception.ResourceNotFoundException;
import com.shriva.personal_finance_manager_backend_java.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Budget addBudget(Budget budget) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        budget.setUser(user);
        return budgetRepository.save(budget);
    }

    public List<Budget> listBudgets() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return budgetRepository.findByUser(user);
    }

    public Budget editBudget(Long id, Budget budgetDetails) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));
        if (!budget.getUser().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new UnauthorizedException("Unauthorized to edit this budget");
        }
        budget.setAmount(budgetDetails.getAmount());
        budget.setStartDate(budgetDetails.getStartDate());
        budget.setEndDate(budgetDetails.getEndDate());
        budget.setCategory(budgetDetails.getCategory());
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));
        if (!budget.getUser().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new UnauthorizedException("Unauthorized to delete this budget");
        }
        budgetRepository.delete(budget);
    }

    public List<Budget> filterBudgets(LocalDate startDate, LocalDate endDate, String category) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return budgetRepository.findByUserAndFilters(user, startDate, endDate, category);
    }

    public double getBudgetUsage(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + budgetId));
        if (!budget.getUser().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new UnauthorizedException("Unauthorized to view this budget usage");
        }
        List<Transaction> transactions = transactionRepository.findByUserAndDateRangeAndCategory(
                budget.getUser(), budget.getStartDate(), budget.getEndDate(), budget.getCategory());
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public boolean isOverspending(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + budgetId));
        double usage = getBudgetUsage(budgetId);
        return usage > budget.getAmount();
    }

    public Map<String, Double> getBudgetSummary(LocalDate startDate, LocalDate endDate) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Budget> budgets = budgetRepository.findByUserAndFilters(user, startDate, endDate, null);
        double totalAllocated = budgets.stream().mapToDouble(Budget::getAmount).sum();
        double totalSpent = budgets.stream()
                .mapToDouble(budget -> getBudgetUsage(budget.getId()))
                .sum();
        double remaining = totalAllocated - totalSpent;

        Map<String, Double> summary = new HashMap<>();
        summary.put("totalAllocated", totalAllocated);
        summary.put("totalSpent", totalSpent);
        summary.put("remaining", remaining);
        return summary;
    }
}