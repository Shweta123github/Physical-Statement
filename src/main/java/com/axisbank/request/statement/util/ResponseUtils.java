package com.axisbank.request.statement.util;

import com.axisbank.request.statement.constants.StatementErrorMessages;
import com.axisbank.request.statement.response.PhysicalStatementResponse;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public static ResponseEntity<PhysicalStatementResponse> errorResponse(StatementErrorMessages errorMessage, String details) {
        return ResponseEntity.status(errorMessage.getStatus().value())
                .body(new PhysicalStatementResponse(errorMessage.getStatus().value(),
                        "failure", null, details));
    }

    public static ResponseEntity<PhysicalStatementResponse> successResponse(String referenceNumber) {
        String message = String.format("Successfully raised request for statement. The reference number is: %s", referenceNumber);
        return ResponseEntity.ok(new PhysicalStatementResponse(StatementErrorMessages.SUCCESS.getStatus().value(),
                "Success", referenceNumber, message));
    }

    public static ResponseEntity<PhysicalStatementResponse> duplicateResponse(String referenceNumber) {
        String message = String.format("Already raised request for same details with reference number: %s", referenceNumber);
        return ResponseEntity.ok(new PhysicalStatementResponse(StatementErrorMessages.DUPLICATE_REQUEST.getStatus().value(),
                "Duplicate", referenceNumber, message));

    }
}

