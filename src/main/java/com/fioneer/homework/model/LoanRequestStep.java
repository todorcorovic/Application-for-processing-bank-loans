package com.fioneer.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class LoanRequestStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier for the loan request step.", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_request_id")
    @NotNull(message = "Loan request must not be null")
    @Schema(description = "The loan request associated with this step.")
    @JsonIgnore
    private LoanRequest loanRequest;

    @NotBlank(message = "Step name must not be blank")
    @Schema(description = "The name of the loan request step.", example = "Document Verification")
    private String name;

    @Min(value = 1, message = "Order must be a positive number")
    @Schema(description = "The order number of the loan request step.", example = "1")
    private int orderNumber;

    @Min(value = 1, message = "Expected duration must be at least 1 day")
    @Schema(description = "The expected duration of the loan request step in days.", example = "2")
    private int expectedDuration;

    @Schema(description = "The actual duration of the loan request step in days, if completed.", example = "3")
    private Integer actualDuration;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The current status of the loan request step.", example = "successful")
    private LoanRequestStepStatus status;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return orderNumber;
    }

    public void setOrder(int order) {
        this.orderNumber = order;
    }

    public int getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(int expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public Integer getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    public LoanRequestStepStatus getStatus() {
        return status;
    }

    public void setStatus(LoanRequestStepStatus status) {
        this.status = status;
    }
}
