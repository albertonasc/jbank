package com.demo.jbank.exception;

import org.springframework.http.ProblemDetail;

public abstract class JBankException extends RuntimeException {

    public JBankException(String message) {
        super(message);
    }

    public JBankException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(500);

        problemDetail.setTitle("JBank Internal Server Error");
        problemDetail.setDetail("Contact JBank Support");

        return problemDetail;
    }
}
