package com.demo.jbank.service;

import com.demo.jbank.Repository.WalletRepository;
import com.demo.jbank.controller.dto.CreateWalletDto;
import com.demo.jbank.entities.Wallet;
import com.demo.jbank.exception.DeleteWalletException;
import com.demo.jbank.exception.WalletDataAlreadyExistException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
}
