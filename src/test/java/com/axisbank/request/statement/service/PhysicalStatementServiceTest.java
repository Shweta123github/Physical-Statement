package com.axisbank.request.statement.service;

import com.axisbank.request.statement.entity.PhysicalStatementDetails;
import com.axisbank.request.statement.repository.PhysicalStatementRequestRepository;
import com.axisbank.request.statement.request.PhysicalStatementRequest;
import com.axisbank.request.statement.response.PhysicalStatementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PhysicalStatementServiceTest {

    @InjectMocks
    private PhysicalStatementService service;

    @Mock
    private PhysicalStatementRequestRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRequestPhysicalStatementSuccess() throws ParseException {
        PhysicalStatementRequest request = createValidRequest();
        String referenceNumber = "PS0000000";

        when(repository.findFirstDuplicate(anyString(), anyString(), any(LocalDate.class), any(Date.class), any(Date.class)))
                .thenReturn(null);
        when(repository.save(any(PhysicalStatementDetails.class)))
                .thenReturn(new PhysicalStatementDetails());

        ResponseEntity<PhysicalStatementResponse> responseEntity = service.requestPhysicalStatement(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify individual field values
        PhysicalStatementResponse actualResponse = responseEntity.getBody();
        assertEquals(200, actualResponse.getResponseCode());
        assertEquals("Success", actualResponse.getStatusMsg());
        assertEquals(referenceNumber, actualResponse.getReferenceNumber());
        assertEquals("Successfully raised request for statement. The reference number is: PS0000000", actualResponse.getMessage());
    }

    @Test
    public void testRequestPhysicalStatementDuplicateRequest() throws ParseException {
        PhysicalStatementRequest request = createValidRequest();
        String referenceNumber = "PS0000000";
        PhysicalStatementDetails existingRequest = new PhysicalStatementDetails();
        existingRequest.setReferenceNumber(referenceNumber);

        when(repository.findFirstDuplicate(anyString(), anyString(), any(LocalDate.class), any(Date.class), any(Date.class)))
                .thenReturn(existingRequest);

        ResponseEntity<PhysicalStatementResponse> responseEntity = service.requestPhysicalStatement(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify individual field values
        PhysicalStatementResponse actualResponse = responseEntity.getBody();
        assertEquals(201, actualResponse.getResponseCode());
        assertEquals("Duplicate", actualResponse.getStatusMsg());
        assertEquals(referenceNumber, actualResponse.getReferenceNumber());
        assertEquals("Already raised request for same details with reference number: PS0000000", actualResponse.getMessage());
    }

    @Test
    public void testGenerateReferenceNumber() {
        when(repository.count()).thenReturn(1L);

        String referenceNumber = service.generateReferenceNumber();

        assertEquals("PS0000001", referenceNumber);
    }
    private PhysicalStatementRequest createValidRequest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse("2023-01-01");
        Date toDate = dateFormat.parse("2023-01-31");

        return new PhysicalStatementRequest(
                "customer123",
                "account123",
                "moduleA",
                "uuid123",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 31)
        );
    }
}





