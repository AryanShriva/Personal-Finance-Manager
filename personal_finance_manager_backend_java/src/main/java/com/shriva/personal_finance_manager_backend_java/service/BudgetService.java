package com.shriva.personal_finance_manager_backend_java.service;

import com.shriva.personal_finance_manager_backend_java.model.Budget;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
}