package com.example.heccore.bank.model;

import com.example.heccore.common.enums.Bank;
import com.example.heccore.common.exception.ErrorCode;
import com.example.heccore.common.exception.HecCustomException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

@Table("bank_accounts")
@NoArgsConstructor
@Getter
public class BankAccount {
    @Id
    private Long accountId;
    private Long userId;
    private Bank bank;
    private Long accountNumber;
    private int balance;
    private boolean isDeleted = false;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public BankAccount(Long userId, Bank bank, Long accountNumber, int balance) {
        this.userId = userId;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // 입금 메소드
    public void deposit(int amount) {
        if (amount <= 0) {
            throw new HecCustomException(ErrorCode.DEPOSIT_IS_NOT_VALID);
        }
        this.balance += amount;
    }

    // 출금 메소드
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new HecCustomException(ErrorCode.WITHDRAW_IS_NOT_VALID);
        }
        this.balance -= amount;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    // 스프링 배치 모듈에서 테이블 컬럼명 매핑할 때 사용
    public void updateForBatchModule(Long accountId, Long userId, boolean isDeleted) {
        this.accountId = accountId;
        this.userId = userId;
        this.isDeleted = isDeleted;
    }
}
