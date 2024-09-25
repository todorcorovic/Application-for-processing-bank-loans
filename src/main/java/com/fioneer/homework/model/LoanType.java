package com.fioneer.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_types")
@Schema(description = "Represents a type of loan offered by the bank")
public class LoanType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the loan type", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Name of the loan type", example = "Mortgage", required = true)
    private String name;

    @OneToMany(mappedBy = "loanType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Schema(description = "List of processing steps associated with this loan type")
    private List<ProcessingStep> steps = new ArrayList<>();

    @OneToMany(mappedBy = "loanType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of loan requests associated with this loan type")
    @JsonIgnore
    private List<LoanRequest> loanRequests = new ArrayList<>();

    @Column(unique = false, nullable = true)
    @Schema(description = "Description of the loan type", example = "Long-term loan for purchasing a home")
    private String description;

    @PreRemove
    private void preRemove() {
        if (!loanRequests.isEmpty()) {
            throw new IllegalStateException("Cannot delete loan type with associated loan requests");
        }
    }

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

    public List<ProcessingStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProcessingStep> steps) {
        this.steps = steps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LoanRequest> getLoanRequests() {
        return loanRequests;
    }

    public void setLoanRequests(List<LoanRequest> loanRequests) {
        this.loanRequests = loanRequests;
    }

    public int getTotalExpectedDuration() {
        return steps.stream().mapToInt(ProcessingStep::getExpectedDuration).sum();
    }
}
