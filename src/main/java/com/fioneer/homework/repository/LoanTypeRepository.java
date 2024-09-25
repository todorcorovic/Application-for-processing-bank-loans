package com.fioneer.homework.repository;

import com.fioneer.homework.model.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
    List<LoanType> findByNameContainingIgnoreCase(String name);
}
