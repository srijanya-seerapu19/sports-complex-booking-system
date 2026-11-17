package com.sportscomplex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sportscomplex.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
