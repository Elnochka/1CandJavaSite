package com.shop1c.repository;

import com.shop1c.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // Пошук користувача за іменем (username)
    Optional<Users> findByCode1C(String code1C);

    // Додатково, якщо треба пошук за email (опціонально)
    Optional<Users> findByEmail(String email);

    boolean existsByCode1C(String code1C);

    boolean existsByEmail(String email);
}
