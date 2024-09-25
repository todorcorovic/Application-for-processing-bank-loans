package com.fioneer.homework.controller;

import com.fioneer.homework.dto.CreateLoanRequestDTO;
import com.fioneer.homework.dto.UpdateStepStatusDTO;
import com.fioneer.homework.model.LoanRequest;
import com.fioneer.homework.model.LoanRequestStatus;
import com.fioneer.homework.service.LoanRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
@Tag(name = "Loan Requests", description = "APIs for managing loan requests, including creation, status updates, and retrieval")
public class LoanRequestController {
    @Autowired
    private LoanRequestService loanRequestService;

    @Operation(summary = "Create a new loan request", description = "Creates a new loan request with the provided details")
    @ApiResponse(responseCode = "200", description = "Loan request created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanRequest.class)))
    @PostMapping
    public ResponseEntity<LoanRequest> createLoanRequest(
            @Parameter(description = "Loan request details", required = true) @RequestBody CreateLoanRequestDTO dto) {
        LoanRequest loanRequest = loanRequestService.createLoanRequest(dto.getLoanTypeId(), dto.getFirstName(), dto.getLastName(), dto.getLoanAmount());
        return ResponseEntity.ok(loanRequest);
    }

    @Operation(summary = "Update loan request step status", description = "Updates the status of a specific step in a loan request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Step status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanRequest.class))),
            @ApiResponse(responseCode = "404", description = "Loan request not found"),
            @ApiResponse(responseCode = "400", description = "Invalid step order or status update")
    })
    @PutMapping("/{id}/steps/{stepOrder}")
    public ResponseEntity<LoanRequest> updateStepStatus(
            @Parameter(description = "ID of the loan request", required = true) @PathVariable Long id,
            @Parameter(description = "Order of the step to update", required = true) @PathVariable int stepOrder,
            @Parameter(description = "New status details", required = true) @RequestBody UpdateStepStatusDTO dto) {
        LoanRequest loanRequest = loanRequestService.updateStepStatus(id, stepOrder, dto.getNewStatus(), dto.getActualDuration());
        return ResponseEntity.ok(loanRequest);
    }

    @Operation(summary = "Get loan request details", description = "Retrieves details of a specific loan request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved loan request details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanRequest.class))),
            @ApiResponse(responseCode = "404", description = "Loan request not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> getLoanRequestDetails(
            @Parameter(description = "ID of the loan request", required = true) @PathVariable Long id) {
        LoanRequest loanRequest = loanRequestService.getLoanRequestDetails(id);
        return ResponseEntity.ok(loanRequest);
    }

    @Operation(summary = "Get loan requests by status", description = "Retrieves a list of loan requests filtered by status (optional)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved loan requests",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanRequest.class)))
    @GetMapping
    public ResponseEntity<List<LoanRequest>> getLoanRequestsByStatus(
            @Parameter(description = "Status to filter loan requests (optional)") @RequestParam(required = false) LoanRequestStatus status) {
        List<LoanRequest> loanRequests = loanRequestService.getLoanRequestsByStatus(status);
        return ResponseEntity.ok(loanRequests);
    }
}