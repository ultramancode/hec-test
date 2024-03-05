package com.example.hecmybatis.bankAccount.mapper;

import com.example.heccore.bank.model.BankAccountVO;
import com.example.heccore.bank.model.BankAccountWithUserNameVO;

import com.example.hecmybatis.bankAccount.dto.request.BankAccountConditionDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BankAccountMapper {

    void createBankAccount(BankAccountVO bankAccountVO);
    void updateBalance(BankAccountVO bankAccountVO);

    void softDeleteBankAccount(BankAccountVO bankAccountVO);

    BankAccountVO getBankAccountById(Long userId);

    List<BankAccountWithUserNameVO> getBankAccountsByUserId(Long userId);

    List<BankAccountWithUserNameVO> getBankAccountsWithUserNameAndOptions(BankAccountConditionDto bankAccountConditionDto);

    boolean isBankAccountNumberExists(Long bankAccountNumber);
}
