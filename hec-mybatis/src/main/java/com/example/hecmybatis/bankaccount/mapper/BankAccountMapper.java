package com.example.hecmybatis.bankaccount.mapper;

import com.example.heccore.bank.model.BankAccountVO;
import com.example.heccore.bank.model.BankAccountWithUserNameVO;

import com.example.heccore.bank.dto.request.BankAccountConditionDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BankAccountMapper {

    void createBankAccount(BankAccountVO bankAccountVO);
    void updateBalance(BankAccountVO bankAccountVO);

    void softDeleteBankAccount(BankAccountVO bankAccountVO);
    void hardDeleteBankAccount(BankAccountVO bankAccountVO);
    void softDeleteBankAccounts(List<Long> accountIds);
    void hardDeleteBankAccounts(List<Long> accountIds);

    BankAccountVO getBankAccountByIdAndDeletedIsFalse(Long accountId);
    BankAccountVO getBankAccountById(Long accountId);

    BankAccountVO getBankAccountByIdAndDeletedIsFalseWithLock(Long accountId);

    List<BankAccountWithUserNameVO> getBankAccountsByUserIdWithDeletedIsFalse(Long userId);
    List<BankAccountWithUserNameVO> getBankAccountsByUserId(Long userId);

    List<BankAccountWithUserNameVO> getBankAccountsWithUserNameAndOptions(BankAccountConditionDto bankAccountConditionDto);

    boolean isBankAccountNumberExists(Long bankAccountNumber);

    void deleteAllBankAccounts();

}
