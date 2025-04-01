package com.demo.jbank.service;

import com.demo.jbank.Repository.DepositRepository;
import com.demo.jbank.Repository.WalletRepository;
import com.demo.jbank.controller.dto.CreateWalletDto;
import com.demo.jbank.controller.dto.DepositMoneyDto;
import com.demo.jbank.entities.Deposit;
import com.demo.jbank.entities.Wallet;
import com.demo.jbank.exception.DeleteWalletException;
import com.demo.jbank.exception.WalletDataAlreadyExistException;
import com.demo.jbank.exception.WalletNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());

        if(walletDb.isPresent()) {
            throw new WalletDataAlreadyExistException("CPF or email already exists");
        }

        var wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());
        wallet.setName(dto.name());

        return walletRepository.save(wallet);
    }

    public boolean deleteWallet(UUID walletId) {
        var wallet = walletRepository.findById(walletId);

        if (wallet.isPresent()) {

            if(wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new DeleteWalletException("the balance is not zero. The current amount is $" + wallet.get().getBalance());
            }

            walletRepository.deleteById(walletId);
        }

        return wallet.isPresent();
    }

    @Transactional
    public void depositMoney(UUID walletId, @Valid DepositMoneyDto dto, String ipAddress) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no wallet with this id"));

        var deposit = new Deposit();
        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.value()));

        walletRepository.save(wallet);

    }
}
