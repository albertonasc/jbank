package com.demo.jbank.exception;

import com.demo.jbank.controller.dto.InvalidParamDto;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JBankException.class)
    public ProblemDetail handleJBankException(JBankException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var invalidParams = e.getFieldErrors().stream()
                        .map(fieldError -> new InvalidParamDto(fieldError.getField(), fieldError.getDefaultMessage()))
                        .toList();

        var problemDetails = ProblemDetail.forStatus(400);

        problemDetails.setTitle("Invalid request parameters");
        problemDetails.setDetail("There is invalid fields on the request");
        problemDetails.setProperty("invalid-params", invalidParams);

        return problemDetails;
    }
}
