package com.example.heccore.bank.model;

import com.example.heccore.common.enums.Bank;
import com.example.heccore.common.model.BaseVO;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BankAccountWithUserNameVO extends BaseVO {

    private Long accountId;
    private Long userId;
    private Bank bank;
    private Long accountNumber;
    private BigDecimal balance;
    private boolean isDeleted = false;
    private String name; // 사용자 이름 추가


    public BankAccountWithUserNameVO(
            Long accountId, Long userId, Bank bank, Long accountNumber, BigDecimal balance, String name) {
        this.accountId = accountId;
        this.userId = userId;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.name = name;
    }
    public void softDelete() {
        this.isDeleted = true;
    }


}
