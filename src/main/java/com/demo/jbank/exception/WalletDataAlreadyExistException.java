package com.demo.jbank.exception;

import org.springframework.http.ProblemDetail;

public class WalletDataAlreadyExistException extends JBankException {

    private final String detail;

    public WalletDataAlreadyExistException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetails =  ProblemDetail.forStatus(422);

        problemDetails.setTitle("Wallet data already exists");
        problemDetails.setDetail(detail);

        return problemDetails;
    }
}
