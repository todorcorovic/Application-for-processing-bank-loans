package com.fioneer.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class CreateLoanRequestDTO {
    @Schema(description = "ID of the loan type", example = "1")
    private Long loanTypeId;

    @Schema(description = "First name of the applicant", example = "John")
    private String firstName;

    @Schema(description = "Last name of the applicant", example = "Doe")
    private String lastName;

    @Schema(description = "Requested loan amount", example = "10000.00")
    private BigDecimal loanAmount;

    // Getters and setters
    public Long getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(Long loanTypeId) {
        this.loanTypeId = loanTypeId;
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
}