package com.shriva.personal_finance_manager_backend_java.repository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
