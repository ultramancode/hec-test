package com.example.hecmybatis.bankAccount.service;

import com.example.heccore.bank.model.BankAccountWithUserNameVO;
import com.example.hecmybatis.bankAccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankAccount.mapper.BankAccountMapper;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountMapper bankAccountMapper;

    public List<BankAccountResponseDto> getAllBankAccountsWithUser() {

        List<BankAccountWithUserNameVO> bankAccountsWithUserVOS = bankAccountMapper.getBankAccountsWithUser();

        return bankAccountsWithUserVOS.stream().map(bankAccountsWithUserVO ->
                new BankAccountResponseDto(bankAccountsWithUserVO.getAccountId(),
                        bankAccountsWithUserVO.getName(), bankAccountsWithUserVO.getBank(),
                        bankAccountsWithUserVO.getAccountNumber(), bankAccountsWithUserVO.getBalance())).collect(
                Collectors.toList());
    }
}
