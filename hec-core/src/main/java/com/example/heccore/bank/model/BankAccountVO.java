package com.example.heccore.bank.model;

import com.example.heccore.common.enums.Bank;
import com.example.heccore.common.exception.ErrorCode;
import com.example.heccore.common.exception.HecCustomException;
import com.example.heccore.common.model.BaseVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
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

    @Builder
    public BankAccountVO(Long userId, Bank bank, Long accountNumber, BigDecimal balance) {
        this.userId = userId;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // 입금 메소드
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new HecCustomException(ErrorCode.DEPOSIT_IS_NOT_VALID);
        }
        this.balance = this.balance.add(amount);
    }

    // 출금 메소드
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new HecCustomException(ErrorCode.WITHDRAW_IS_NOT_VALID);
        }
        if (amount.compareTo(this.balance) > 0) {
            throw new HecCustomException(ErrorCode.BALANCE_IS_NOT_VALID);
        }
        this.balance = this.balance.subtract(amount);
    }
    public void softDelete() {
        this.isDeleted = true;
    }
}
