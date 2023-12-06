package com.axisbank.request.statement.service;

import com.axisbank.request.statement.entity.PhysicalStatementDetails;
import com.axisbank.request.statement.repository.PhysicalStatementRequestRepository;
import com.axisbank.request.statement.request.PhysicalStatementRequest;
import com.axisbank.request.statement.response.PhysicalStatementResponse;
import com.axisbank.request.statement.util.ResponseUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Log4j2
public class PhysicalStatementService {
    private final PhysicalStatementRequestRepository repository;

    @Autowired
    public PhysicalStatementService(PhysicalStatementRequestRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ResponseEntity<PhysicalStatementResponse> requestPhysicalStatement(PhysicalStatementRequest request) {
        String referenceNumber = generateReferenceNumber();
        PhysicalStatementDetails existingRequest = checkForExistingRequest(request);

        if (existingRequest != null) {
            return ResponseUtils.duplicateResponse(existingRequest.getReferenceNumber());
        }

        PhysicalStatementDetails physicalStatementDetails = getPhysicalStatementDetails(request, referenceNumber);
        repository.save(physicalStatementDetails);

        log.info("Physical statement request processed successfully. Reference number: {}", referenceNumber);
        return ResponseUtils.successResponse(referenceNumber);
    }

    private PhysicalStatementDetails checkForExistingRequest(PhysicalStatementRequest request) {
        Date fromDate = parseDate(request.fromDate().toString());
        Date toDate = parseDate(request.toDate().toString());

        return repository.findFirstDuplicate(
                request.customerId(), request.accountNumber(), LocalDate.now(), fromDate, toDate);
    }

    private PhysicalStatementDetails getPhysicalStatementDetails(PhysicalStatementRequest request, String referenceNumber) {
        PhysicalStatementDetails physicalStatementDetails = new PhysicalStatementDetails();
        physicalStatementDetails.setCustomerId(request.customerId());
        physicalStatementDetails.setAccountNumber(request.accountNumber());
        physicalStatementDetails.setModuleName(request.ModuleName());
        physicalStatementDetails.setRequestUUID(request.requestUUID());
        physicalStatementDetails.setFromDate(parseDate(request.fromDate().toString()));
        physicalStatementDetails.setToDate(parseDate(request.toDate().toString()));
        physicalStatementDetails.setRequestedDate(LocalDate.now());
        physicalStatementDetails.setReferenceNumber(referenceNumber);
        return physicalStatementDetails;
    }

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }
    @Transactional
    public String generateReferenceNumber() {
        AtomicLong referenceNumberCounter = new AtomicLong(repository.count());
        return "PS" + String.format("%07d", referenceNumberCounter.getAndIncrement());
    }
}


