package com.example.hecmybatis.bankAccount.dto.response;


import com.example.heccore.common.enums.Bank;
import java.math.BigDecimal;
import lombok.Getter;

public record BankAccountResponseDto(Long accountId, String name, Bank bank, Long accountNumber, int balance) {
}
