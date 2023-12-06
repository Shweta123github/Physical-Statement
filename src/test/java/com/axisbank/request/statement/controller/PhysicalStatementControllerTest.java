package com.axisbank.request.statement.controller;

import com.axisbank.request.statement.constants.StatementErrorMessages;
import com.axisbank.request.statement.request.PhysicalStatementRequest;
import com.axisbank.request.statement.response.PhysicalStatementResponse;
import com.axisbank.request.statement.service.PhysicalStatementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PhysicalStatementControllerTest {

    @InjectMocks
    private PhysicalStatementController controller;

    @Mock
    private PhysicalStatementService physicalStatementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidRequest() {
        PhysicalStatementRequest validRequest = new PhysicalStatementRequest(
                "customer123",
                "account123",
                "moduleA",
                "uuid123",
                LocalDate.now().minusDays(10),
                LocalDate.now()
        );

        when(physicalStatementService.requestPhysicalStatement(validRequest))
                .thenReturn(ResponseEntity.ok(new PhysicalStatementResponse(200, "Success", "Message", "Data")));



        ResponseEntity<PhysicalStatementResponse> response = controller.requestPhysicalStatement(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testInvalidDateRange() {
        PhysicalStatementRequest request = new PhysicalStatementRequest(
                "customer123",
                "account123",
                "moduleA",
                "uuid123",
                LocalDate.now(),
                LocalDate.now().minusDays(1)
        );

        ResponseEntity<PhysicalStatementResponse> response = controller.requestPhysicalStatement(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(StatementErrorMessages.INVALID_DATE_RANGE.getMessage(), response.getBody().getMessage());
    }

    @Test
    public void testSameDates() {
        LocalDate currentDate = LocalDate.now();
        PhysicalStatementRequest request = new PhysicalStatementRequest(
                "customer123",
                "account123",
                "moduleA",
                "uuid123",
                currentDate,
                currentDate
        );

        ResponseEntity<PhysicalStatementResponse> response = controller.requestPhysicalStatement(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(StatementErrorMessages.SAME_DATES.getMessage(), response.getBody().getMessage());
    }

    @Test
    public void testFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        PhysicalStatementRequest request = new PhysicalStatementRequest(
                "customer123",
                "account123",
                "moduleA",
                "uuid123",
                futureDate,
                futureDate
        );

        ResponseEntity<PhysicalStatementResponse> response = controller.requestPhysicalStatement(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("'fromDate' and 'toDate' cannot be the same,  2.'fromDate' cannot be in the 'future'", response.getBody().getMessage());
    }

    @Test
    public void testBadRequest() {
        PhysicalStatementRequest invalidRequest = new PhysicalStatementRequest(
                null,
                "account123",
                "moduleA",
                "uuid123",
                LocalDate.now(),
                LocalDate.now()
        );

        ResponseEntity<PhysicalStatementResponse> response = controller.requestPhysicalStatement(invalidRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("'fromDate' and 'toDate' cannot be the same", response.getBody().getMessage());
    }

    @Test
    public void testInternalServerError() {
        PhysicalStatementRequest validRequest = new PhysicalStatementRequest(
                "customer123",
                "account123",
                "moduleA",
                "uuid123",
                LocalDate.now().minusDays(10),
                LocalDate.now()
        );

        when(physicalStatementService.requestPhysicalStatement(validRequest))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<PhysicalStatementResponse> response = controller.requestPhysicalStatement(validRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getMessage());
    }
}
