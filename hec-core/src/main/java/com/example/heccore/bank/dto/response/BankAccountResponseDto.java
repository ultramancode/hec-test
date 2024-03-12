package com.example.heccore.bank.dto.response;


import com.example.heccore.common.enums.Bank;

public record BankAccountResponseDto(Long accountId, String name, Bank bank, Long accountNumber, int balance) {
}
