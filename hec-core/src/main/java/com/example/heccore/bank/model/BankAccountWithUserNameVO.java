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
    private int balance;
    private boolean isDeleted;
    private String name; // 사용자 이름 추가

}
