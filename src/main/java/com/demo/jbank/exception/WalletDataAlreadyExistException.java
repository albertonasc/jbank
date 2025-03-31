package com.demo.jbank.exception;

public class WalletDataAlreadyExistException extends JBankException {

    public WalletDataAlreadyExistException(String message) {
        super(message);
    }
}
