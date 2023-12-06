package com.axisbank.request.statement.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PhysicalStatementResponse {

    @Schema(description = "response code")
    private int responseCode;
    @Schema(description = "Status of the response (e.g., 'success' or 'failure' or 'duplicate')")
    private String statusMsg;
    @Schema(description = "Reference number associated with the request")
    private String referenceNumber;
    @Schema(description = "Message explaining the response")
    private String message;

}
