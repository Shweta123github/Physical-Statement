package com.axisbank.request.statement.repository;
import com.axisbank.request.statement.entity.PhysicalStatementDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface PhysicalStatementRequestRepository extends CrudRepository<PhysicalStatementDetails, Long> {
    boolean existsByCustomerIdAndAccountNumberAndRequestedDateAndFromDateAndToDate(
            String customerId, String accountNumber, LocalDate requestedDate, Date fromDate, Date toDate);

    @Query("SELECT p FROM PhysicalStatementDetails p WHERE p.customerId = :customerId " +
            "AND p.accountNumber = :accountNumber " +
            "AND p.requestedDate = :requestedDate " +
            "AND p.fromDate = :fromDate " +
            "AND p.toDate = :toDate " +
            "ORDER BY p.id ASC")
    PhysicalStatementDetails findFirstDuplicate(@Param("customerId") String customerId,
                                                @Param("accountNumber") String accountNumber,
                                                @Param("requestedDate") LocalDate requestedDate,
                                                @Param("fromDate") Date fromDate,
                                                @Param("toDate") Date toDate);
}


