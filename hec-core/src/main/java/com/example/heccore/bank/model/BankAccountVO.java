package com.example.heccore.bank.model;

import com.example.heccore.common.enums.Bank;
import com.example.heccore.common.model.BaseVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BankAccountVO extends BaseVO {

    private Long accountId;
    private Long userId;
    private Bank bank;
    private Long accountNumber;
    private BigDecimal balance;
    private boolean isDeleted = false;

    public BankAccountVO(
            Long accountId, Long userId, Bank bank, Long accountNumber, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
