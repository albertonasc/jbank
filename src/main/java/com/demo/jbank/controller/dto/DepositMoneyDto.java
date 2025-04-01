package com.demo.jbank.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositMoneyDto(@NotNull @DecimalMin("5.00") BigDecimal value) {
}
