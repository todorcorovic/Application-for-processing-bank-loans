package com.fioneer.homework.repository;

import com.fioneer.homework.model.LoanRequest;
import com.fioneer.homework.model.LoanRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    List<LoanRequest> findByStatus(LoanRequestStatus status);
    boolean existsByLoanTypeId(Long loanTypeId);}
