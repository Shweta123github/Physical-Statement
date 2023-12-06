package com.axisbank.request.statement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "physical_statement_request_details",indexes = {
        @Index(name = "idx_account_number", columnList = "account_number"),
        @Index(name = "idx_customer_id", columnList = "customer_id")
})
public class PhysicalStatementDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "account_number",length = 50)
    private String accountNumber;

    @Column(length =50, nullable = false)
    private String ModuleName;

    @Column(name="request_UUID",length =200, nullable = false)
    private String requestUUID;

    @Temporal(TemporalType.DATE)
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    private Date toDate;

    @Column(name = "reference_number", length = 200, nullable = false)
    private String referenceNumber;

    @JsonIgnore
    private LocalDate requestedDate = LocalDate.now();
    @JsonIgnore
    private String courierName;
    @JsonIgnore
    @Column(name = "AWB_ID",length = 50)
    private String AWBID;
    @JsonIgnore
    private LocalDateTime courierDate;
    @JsonIgnore
    @Column(name = "status", length = 50)
    private String status;

    @JsonIgnore
    @Column(length = 50)
    private String Flexi_Field_1;
    @JsonIgnore
    @Column(length = 50)
    private String Flexi_Field_2;
    @JsonIgnore
    private String Flexi_Field_3;
}

