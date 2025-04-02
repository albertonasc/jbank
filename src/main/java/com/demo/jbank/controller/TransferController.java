package com.demo.jbank.controller;

import com.demo.jbank.controller.dto.TransferMoneyDto;
import com.demo.jbank.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferMoneyDto dto) {

        transferService.transferMoney(dto);

        return ResponseEntity.ok().build();
    }

}
