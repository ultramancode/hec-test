package com.example.hecmybatis.bankAccount.dto.request;

import com.example.heccore.common.enums.Bank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BankAccountConditionDto {
    private Long userId;
    private Long accountNumber;
    private Bank bank;
    private String name;
    private BigDecimal minBalance;
    private BigDecimal maxBalance;
    private String orderBy;
    private String orderDirection;
    private int page;
    private int size;
    private int offset;

    public BankAccountConditionDto(int page, int size) {
        this.page = page;
        this.size = size;
        this.offset = (page - 1) * size;
    }

    // 기본값으로 페이지를 1, 사이즈를 3으로 설정하는 생성자
    public BankAccountConditionDto() {
        this(1, 3);
    }

    // offset 값을 계산하여 반환하는 getter 메서드
    public int getOffset() {
        return (page - 1) * size;
    }

}
