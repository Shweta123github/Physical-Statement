package com.axisbank.request.statement.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PhysicalStatementRequest(
        @NotBlank(message = "customerId must not be blank") String customerId,
        @NotEmpty(message = "accountNumber must not be blank") String accountNumber,
        @NotEmpty(message = "moduleName is required") String ModuleName,
        @NotEmpty(message = "requestUUID is required") String requestUUID,
        @NotNull(message = "fromDate is required") LocalDate fromDate,
        @NotNull(message = "toDate is required") LocalDate toDate

) {
}


