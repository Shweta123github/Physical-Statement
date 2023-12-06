package com.axisbank.request.statement.exception;

import lombok.Data;

@Data
public class AxisException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String axisErrorCode;

    public AxisException() {
    }
    public AxisException(String axisErrorCode) {
        this.axisErrorCode = axisErrorCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AxisException (");
        ;
        sb.append("axisErrorCode = ").append(axisErrorCode);
        sb.append(")");
        return sb.toString();
    }

}