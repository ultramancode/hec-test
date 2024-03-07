package com.example.hecmybatis.bankAccount.dto.request;

import com.example.heccore.common.enums.Bank;

public record BankAccountRequestDto(Long userId, Bank bank, int balance) {

}
