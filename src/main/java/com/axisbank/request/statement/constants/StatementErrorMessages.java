package com.axisbank.request.statement.constants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = false)
public enum StatementErrorMessages {

    SUCCESS(HttpStatus.OK, "Success"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    DUPLICATE_REQUEST(HttpStatus.CREATED ,"Duplicate"),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "Invalid date range:1.'toDate' cannot be before 'fromDate'"),
    SAME_DATES(HttpStatus.BAD_REQUEST, "'fromDate' and 'toDate' cannot be the same"),
    FUTURE_DATE(HttpStatus.BAD_REQUEST, " 2.'fromDate' cannot be in the 'future'"),
    INVALID_DATE_PATTERN(HttpStatus.BAD_REQUEST, "Invalid date pattern. It should be in yyyy-MM-dd");

    private final HttpStatus status;
    private final String message;
}

