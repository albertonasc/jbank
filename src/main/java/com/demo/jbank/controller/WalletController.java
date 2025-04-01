package com.demo.jbank.controller;

import com.demo.jbank.controller.dto.CreateWalletDto;
import com.demo.jbank.controller.dto.DepositMoneyDto;
import com.demo.jbank.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto dto) {

        var wallet = walletService.createWallet(dto);

        return ResponseEntity.created(URI.create("/wallets/" + wallet.getWalletId().toString())).build();
    }

    @PostMapping(path = "/{walletId}/deposit")
    public ResponseEntity<Void> depositMoney(@PathVariable("walletId") UUID walletId,
                                             @RequestBody @Valid DepositMoneyDto dto,
                                             HttpServletRequest servletRequest) {

        walletService.depositMoney(
                walletId,
                dto,
                servletRequest.getAttribute("x-user-ip").toString());

        return ResponseEntity.ok().build();
    }

}
