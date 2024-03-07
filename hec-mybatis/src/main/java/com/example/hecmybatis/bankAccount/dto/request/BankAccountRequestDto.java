package com.example.hecmybatis.bankAccount.dto.request;

import com.example.heccore.common.enums.Bank;
import java.math.BigDecimal;

public record BankAccountRequestDto(Long userId, Bank bank, int balance) {

}
