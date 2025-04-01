package com.demo.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class WalletNotFoundException extends JBankException{

    private final String detail;

    public WalletNotFoundException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail =ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Wallet not found");
        problemDetail.setDetail(detail);

        return problemDetail;
    }
}
