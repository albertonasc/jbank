package com.demo.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class DeleteWalletException extends JBankException{

    private final String detail;

    public DeleteWalletException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("You cannot delete this wallet");
        problemDetail.setDetail(detail);

        return problemDetail;
    }
}
