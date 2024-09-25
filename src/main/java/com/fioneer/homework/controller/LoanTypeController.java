package com.fioneer.homework.controller;

import com.fioneer.homework.model.LoanType;
import com.fioneer.homework.service.LoanTypeService;
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
@RequestMapping("/api/loan-types")
@Tag(name = "Loan Types", description = "Loan Type management APIs")
public class LoanTypeController {

    @Autowired
    private LoanTypeService loanTypeService;

    @Operation(summary = "Get all loan types", description = "Retrieves a list of all loan types")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved loan types",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanType.class)))
    @GetMapping
    public ResponseEntity<List<LoanType>> getAllLoanTypes() {
        List<LoanType> loanTypes = loanTypeService.getAllLoanTypes();
        return ResponseEntity.ok(loanTypes);
    }

    @Operation(summary = "Get a loan type by ID", description = "Retrieves a loan type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the loan type",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanType.class))),
            @ApiResponse(responseCode = "404", description = "Loan type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoanType> getLoanTypeById(
            @Parameter(description = "ID of the loan type to retrieve") @PathVariable Long id) {
        return loanTypeService.getLoanTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new loan type", description = "Creates a new loan type")
    @ApiResponse(responseCode = "201", description = "Loan type created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanType.class)))
    @PostMapping
    public ResponseEntity<LoanType> createLoanType(
            @Parameter(description = "Loan type to create", required = true) @RequestBody LoanType loanType) {
        LoanType createdLoanType = loanTypeService.createLoanType(loanType);
        return ResponseEntity.status(201).body(createdLoanType);
    }

    @Operation(summary = "Update a loan type", description = "Updates an existing loan type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan type updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanType.class))),
            @ApiResponse(responseCode = "404", description = "Loan type not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LoanType> updateLoanType(
            @Parameter(description = "ID of the loan type to update") @PathVariable Long id,
            @Parameter(description = "Updated loan type details", required = true) @RequestBody LoanType loanTypeDetails) {

        LoanType updatedLoanType = loanTypeService.updateLoanType(id, loanTypeDetails);
        return ResponseEntity.ok(updatedLoanType);
    }

    @Operation(summary = "Delete a loan type", description = "Deletes a loan type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Loan type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Loan type not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanType(
            @Parameter(description = "ID of the loan type to delete") @PathVariable Long id) {
        loanTypeService.deleteLoanType(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search loan types by name", description = "Searches for loan types by name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved matching loan types",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanType.class)))
    @GetMapping("/search")
    public ResponseEntity<List<LoanType>> searchLoanTypesByName(
            @Parameter(description = "Name to search for", required = true) @RequestParam String name) {
        List<LoanType> loanTypes = loanTypeService.searchLoanTypesByName(name);
        return ResponseEntity.ok(loanTypes);
    }
}