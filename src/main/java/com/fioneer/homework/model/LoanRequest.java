package com.fioneer.homework.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Schema(description = "Represents a loan request in the system")
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the loan request", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    @Schema(description = "The type of loan associated with this request")
    private LoanType loanType;

    @Schema(description = "First name of the loan applicant", example = "John")
    private String firstName;

    @Schema(description = "Last name of the loan applicant", example = "Doe")
    private String lastName;

    @Schema(description = "The amount of loan requested", example = "10000.00")
    private BigDecimal loanAmount;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Current status of the loan request", example = "PROCESSING")
    private LoanRequestStatus status;

    @OneToMany(mappedBy = "loanRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of steps in the loan request process")
    private List<LoanRequestStep> steps = new ArrayList<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LoanRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LoanRequestStatus status) {
        this.status = status;
    }

    public List<LoanRequestStep> getSteps() {
        return steps;
    }

    public void setSteps(List<LoanRequestStep> steps) {
        this.steps = steps;
    }

    //Calculates the total expected duration of all steps in days.
    public int getTotalExpectedDuration() {
        return steps.stream()
                .mapToInt(LoanRequestStep::getExpectedDuration)
                .sum();
    }

     //Calculates the total actual duration of completed steps in days.
    public int getTotalActualDuration() {
        return steps.stream()
                .filter(step -> step.getStatus() == LoanRequestStepStatus.SUCCESSFUL)
                .mapToInt(LoanRequestStep::getActualDuration)
                .sum();
    }
}
