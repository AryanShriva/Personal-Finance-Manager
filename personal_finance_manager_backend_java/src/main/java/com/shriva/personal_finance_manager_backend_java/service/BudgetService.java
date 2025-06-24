package com.shriva.personal_finance_manager_backend_java.service;

import com.shriva.personal_finance_manager_backend_java.model.Budget;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

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
                .orElseThrow(() -> new RuntimeException("Budget not found: " + id));
        if (!budget.getUser().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new RuntimeException("Unauthorized to edit this budget");
        }
        budget.setAmount(budgetDetails.getAmount());
        budget.setStartDate(budgetDetails.getStartDate());
        budget.setEndDate(budgetDetails.getEndDate());
        budget.setCategory(budgetDetails.getCategory());
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found: " + id));
        if (!budget.getUser().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new RuntimeException("Unauthorized to delete this budget");
        }
        budgetRepository.delete(budget);
    }

    public List<Budget> filterBudgets(LocalDate startDate, LocalDate endDate, String category) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return budgetRepository.findByUserAndFilters(user, startDate, endDate, category);
    }
}