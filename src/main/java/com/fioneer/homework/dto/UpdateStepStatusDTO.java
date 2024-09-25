package com.fioneer.homework.dto;

import com.fioneer.homework.model.LoanRequestStepStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateStepStatusDTO {

    @Schema(description = "The new status of the loan request step", example = "SUCCESSFUL", required = true)
    private LoanRequestStepStatus newStatus;

    @Schema(description = "The actual duration of the step in days", example = "5", required = true, minimum = "0")
    private int actualDuration;

    // Getters and setters
    public LoanRequestStepStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(LoanRequestStepStatus newStatus) {
        this.newStatus = newStatus;
    }

    public int getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(int actualDuration) {
        this.actualDuration = actualDuration;
    }
}