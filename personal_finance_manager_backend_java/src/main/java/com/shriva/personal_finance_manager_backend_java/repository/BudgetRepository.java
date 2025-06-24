package com.shriva.personal_finance_manager_backend_java.repository;

import com.shriva.personal_finance_manager_backend_java.model.Budget;
import com.shriva.personal_finance_manager_backend_java.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUser(User user);

    @Query("SELECT b FROM Budget b WHERE b.user = :user " +
            "AND (:startDate IS NULL OR b.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR b.endDate <= :endDate) " +
            "AND (:category IS NULL OR b.category = :category)")
    List<Budget> findByUserAndFilters(@Param("user") User user,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate,
                                      @Param("category") String category);
}