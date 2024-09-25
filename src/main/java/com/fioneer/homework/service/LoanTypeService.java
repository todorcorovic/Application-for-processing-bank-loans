package com.fioneer.homework.service;

import com.fioneer.homework.model.LoanType;
import com.fioneer.homework.model.ProcessingStep;
import com.fioneer.homework.repository.LoanTypeRepository;
import com.fioneer.homework.repository.LoanRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanTypeService {

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    public List<LoanType> getAllLoanTypes() {
        return loanTypeRepository.findAll();
    }

    public Optional<LoanType> getLoanTypeById(Long id) {
        return loanTypeRepository.findById(id);
    }

    public LoanType createLoanType(LoanType loanType) {
        // Set the loanType reference in each step
        for (ProcessingStep step : loanType.getSteps()) {
            step.setLoanType(loanType);
        }
        return loanTypeRepository.save(loanType);
    }

    public LoanType updateLoanType(Long id, LoanType loanTypeDetails) {
        LoanType existingLoanType = loanTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanType not found"));

        // Check if there are any existing loan requests for this loan type
        boolean hasLoanRequests = loanRequestRepository.existsByLoanTypeId(id);
        if (hasLoanRequests) {
            throw new RuntimeException("Cannot update loan type with existing loan requests");
        }

        // Update basic details
        existingLoanType.setName(loanTypeDetails.getName());
        existingLoanType.setDescription(loanTypeDetails.getDescription());

        // Maintain existing steps and update or add new steps
        for (ProcessingStep stepDetails : loanTypeDetails.getSteps()) {
            ProcessingStep existingStep = existingLoanType.getSteps().stream()
                    .filter(step -> step.getId().equals(stepDetails.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingStep != null) {
                // Update existing step
                existingStep.setName(stepDetails.getName());
                existingStep.setOrderNumber(stepDetails.getOrderNumber());
                existingStep.setExpectedDuration(stepDetails.getExpectedDuration());
            } else {
                // Add new step
                stepDetails.setLoanType(existingLoanType);
                existingLoanType.getSteps().add(stepDetails);
            }
        }

        return loanTypeRepository.save(existingLoanType);
    }

    public void deleteLoanType(Long id) {
        loanTypeRepository.deleteById(id);
    }

    public List<LoanType> searchLoanTypesByName(String name) {
        return loanTypeRepository.findByNameContainingIgnoreCase(name);
    }

}
