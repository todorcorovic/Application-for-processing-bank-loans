package com.fioneer.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "processing_steps")
@Schema(description = "Entity representing a processing step in a loan request.")
public class ProcessingStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the processing step.", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Name of the processing step.", example = "Credit Check", required = true)
    private String name;

    @Column(nullable = false)
    @Schema(description = "Order number of the processing step, indicating its sequence.", example = "1")
    private int orderNumber;

    @Column(nullable = false)
    @Schema(description = "Expected duration of the processing step in days.", example = "3")
    private int expectedDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id")
    @Schema(description = "The loan type associated with this processing step.")
    @JsonIgnore
    private LoanType loanType;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(int expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }
}