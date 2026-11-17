package com.sportscomplex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sportscomplex.model.Court;

public interface CourtRepository extends JpaRepository<Court, Long> {
    List<Court> findAllByActiveTrue();
    List<Court> findAllBySportIgnoreCaseAndActiveTrue(String sport);
    boolean existsByNameIgnoreCase(String name);
}
