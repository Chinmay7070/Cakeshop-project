package com.nt.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.model.User;

public interface IUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}