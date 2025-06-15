package com.shriva.personal_finance_manager_backend_java.repository;

import com.shriva.personal_finance_manager_backend_java.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
