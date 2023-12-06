package com.axisbank.request.statement.controller;

import com.axisbank.request.statement.constants.StatementErrorMessages;
import com.axisbank.request.statement.request.PhysicalStatementRequest;
import com.axisbank.request.statement.response.PhysicalStatementResponse;
import com.axisbank.request.statement.service.PhysicalStatementService;
import com.axisbank.request.statement.util.ResponseUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statement")
@Log4j2
public class PhysicalStatementController {
    @Autowired
    private PhysicalStatementService physicalStatementService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = PhysicalStatementResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = PhysicalStatementResponse.class))),
            @ApiResponse(responseCode = "201", description = "Duplicate Request",
                    content = @Content(schema = @Schema(implementation = PhysicalStatementResponse.class)))
    })
    @PostMapping("/physical-statement")
    public ResponseEntity<PhysicalStatementResponse> requestPhysicalStatement(
            @Valid @RequestBody PhysicalStatementRequest request) {
        List<String> validationErrors = validateRequest(request);
        if (!validationErrors.isEmpty()) {
            return ResponseUtils.errorResponse(StatementErrorMessages.BAD_REQUEST, String.join(", ", validationErrors));
        }
        try {
            log.info("Physical statement request processed successfully.");
            return physicalStatementService.requestPhysicalStatement(request);
        } catch (Exception ex) {
            return ResponseUtils.errorResponse(StatementErrorMessages.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private List<String> validateRequest(PhysicalStatementRequest request) {
        List<String> validationErrors = new ArrayList<>();

        try {
            LocalDate fromDate = request.fromDate();
            LocalDate toDate = request.toDate();

            if (toDate.isBefore(fromDate)) {
                validationErrors.add(StatementErrorMessages.INVALID_DATE_RANGE.getMessage());
            }
            if (fromDate.isEqual(toDate)) {
                validationErrors.add(StatementErrorMessages.SAME_DATES.getMessage());
            }
            if (fromDate.isAfter(LocalDate.now())) {
                validationErrors.add(StatementErrorMessages.FUTURE_DATE.getMessage());
            }
        } catch (Exception ex) {
            validationErrors.add(StatementErrorMessages.BAD_REQUEST.getMessage());
        }

        return validationErrors;
    }
}
