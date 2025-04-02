package com.demo.jbank.service;

import com.demo.jbank.Repository.TransferRepository;
import com.demo.jbank.Repository.WalletRepository;
import com.demo.jbank.controller.dto.TransferMoneyDto;
import com.demo.jbank.entities.Transfer;
import com.demo.jbank.entities.Wallet;
import com.demo.jbank.exception.TransferException;
import com.demo.jbank.exception.WalletNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(@Valid TransferMoneyDto dto) {

        var sender = walletRepository.findById(dto.sender())
                .orElseThrow(() -> new WalletNotFoundException("sender does not exist"));


        var receiver = walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("receiver does not exist"));

        if(sender.getBalance().compareTo(dto.value()) == -1) {
            throw new TransferException("insufficient balance, you current balance is $" + sender.getBalance());
        }

        updateWallets(dto, sender, receiver);
        persistTransfer(dto, receiver, sender);
    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));
        walletRepository.save(sender);
        walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDto dto, Wallet receiver, Wallet sender) {
        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());
        transferRepository.save(transfer);
    }
}
