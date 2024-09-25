package com.fioneer.homework.service;

import com.fioneer.homework.model.*;
import com.fioneer.homework.repository.LoanTypeRepository;
import com.fioneer.homework.repository.LoanRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanRequestService {

    // Inject the LoanRequestRepository to interact with loan request data
    @Autowired
    private LoanRequestRepository loanRequestRepository;

    // Inject the LoanTypeRepository to interact with loan type data
    @Autowired
    private LoanTypeRepository loanTypeRepository;

    /**
     * Creates a new loan request with initial status and steps based on the loan type.
     *
     * @param loanTypeId The ID of the loan type.
     * @param firstName The first name of the applicant.
     * @param lastName The last name of the applicant.
     * @param loanAmount The amount of the loan requested.
     * @return The created LoanRequest.
     */
    public LoanRequest createLoanRequest(Long loanTypeId, String firstName, String lastName, BigDecimal loanAmount) {
        // Find the loan type by ID, throw exception if not found
        LoanType loanType = loanTypeRepository.findById(loanTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Loan type not found"));

        // Create a new LoanRequest object and set its attributes
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setLoanType(loanType);
        loanRequest.setFirstName(firstName);
        loanRequest.setLastName(lastName);
        loanRequest.setLoanAmount(loanAmount);
        loanRequest.setStatus(LoanRequestStatus.PROCESSING);

        // Create steps based on loan type and set initial status to PENDING
        List<LoanRequestStep> steps = loanType.getSteps().stream()
                .map(step -> {
                    LoanRequestStep requestStep = new LoanRequestStep();
                    requestStep.setName(step.getName());
                    requestStep.setOrder(step.getOrderNumber());
                    requestStep.setExpectedDuration(step.getExpectedDuration());
                    requestStep.setStatus(LoanRequestStepStatus.PENDING);
                    requestStep.setLoanRequest(loanRequest);
                    return requestStep;
                })
                .collect(Collectors.toList());

        loanRequest.setSteps(steps);

        return loanRequestRepository.save(loanRequest);
    }

    /**
     * Updates the status of a specific step within a loan request.
     *
     * @param loanRequestId The ID of the loan request.
     * @param stepOrder The order of the step to be updated.
     * @param newStatus The new status of the step.
     * @param actualDuration The actual duration of the step.
     * @return The updated LoanRequest.
     */
    public LoanRequest updateStepStatus(Long loanRequestId, int stepOrder, LoanRequestStepStatus newStatus, int actualDuration) {
        // Find the loan request by ID, throw exception if not found
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Loan request not found"));

        // Find the step within the loan request by its order, throw exception if not found
        LoanRequestStep step = loanRequest.getSteps().stream()
                .filter(s -> s.getOrder() == stepOrder)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Step not found"));

        // Check if the step is already completed, throw exception if it is
        if (step.getStatus() != LoanRequestStepStatus.PENDING) {
            throw new IllegalStateException("Cannot update completed step");
        }

        // Check if all previous steps are completed, throw exception if not
        if (loanRequest.getSteps().stream()
                .filter(s -> s.getOrder() < stepOrder)
                .anyMatch(s -> s.getStatus() == LoanRequestStepStatus.PENDING)) {
            throw new IllegalStateException("Cannot update step when previous steps are pending");
        }

        // Update the step's status and actual duration
        step.setStatus(newStatus);
        step.setActualDuration(actualDuration);

        // Update the status of the loan request based on its steps
        updateLoanRequestStatus(loanRequest);

        return loanRequestRepository.save(loanRequest);
    }

    //Updates the status of a loan request based on the statuses of its steps.
    private void updateLoanRequestStatus(LoanRequest loanRequest) {
        // Check if all steps are successful
        boolean allStepsSuccessful = loanRequest.getSteps().stream()
                .allMatch(step -> step.getStatus() == LoanRequestStepStatus.SUCCESSFUL);

        // Check if any step has failed
        boolean anyStepFailed = loanRequest.getSteps().stream()
                .anyMatch(step -> step.getStatus() == LoanRequestStepStatus.FAILED);

        // Update the loan request status based on the steps' statuses
        if (allStepsSuccessful) {
            loanRequest.setStatus(LoanRequestStatus.APPROVED);
        } else if (anyStepFailed) {
            loanRequest.setStatus(LoanRequestStatus.REJECTED);
        } else {
            loanRequest.setStatus(LoanRequestStatus.PROCESSING);
        }
    }

    // Retrieves the details of a specific loan request.
    public LoanRequest getLoanRequestDetails(Long loanRequestId) {
        // Find the loan request by ID, throw exception if not found
        return loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Loan request not found"));
    }


    // Retrieves loan requests by their status.
    public List<LoanRequest> getLoanRequestsByStatus(LoanRequestStatus status) {
        // Find and return loan requests by their status
        return loanRequestRepository.findByStatus(status);
    }
}
