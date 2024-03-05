package com.example.hecmybatis.bankAccount.mapper;

import com.example.heccore.bank.model.BankAccountVO;
import com.example.heccore.bank.model.BankAccountWithUserNameVO;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BankAccountMapper {

    @Select("SELECT * FROM bank_account")
    List<BankAccountVO> selectAllBankAccounts();

    List<BankAccountWithUserNameVO> getBankAccountsWithUser();
}
