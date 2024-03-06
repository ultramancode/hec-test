package com.example.hecmybatis.bankAccount.service;

import com.example.heccore.bank.model.BankAccountVO;
import com.example.heccore.bank.model.BankAccountWithUserNameVO;
import com.example.heccore.common.exception.ErrorCode;
import com.example.heccore.common.exception.HecCustomException;
import com.example.heccore.user.model.UserVO;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountAmountRequestDto;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountConditionDto;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankAccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankAccount.mapper.BankAccountMapper;
import com.example.hecmybatis.user.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountMapper bankAccountMapper;
    private final UserService userService;


    @Transactional
    public void createBankAccount(BankAccountRequestDto bankAccountRequestDto) {
        Long bankAccountNumber = generateBankAccountNumber();
        if (isBankAccountNumberExists(bankAccountNumber)) {
            BankAccountVO bankAccountVO =
                    BankAccountVO.builder()
                            .accountNumber(bankAccountNumber)
                            .bank(bankAccountRequestDto.bank())
                            .balance(bankAccountRequestDto.balance())
                            .userId(bankAccountRequestDto.userId())
                            .build();
            bankAccountMapper.createBankAccount(bankAccountVO);
        } else {
            throw new HecCustomException(ErrorCode.ACCOUNT_NUMBER_IS_ALREADY_EXIST);
        }
    }

    // 락이 걸린 getBankAccountByIdWithLock로 조회 후 수정 (동시성 이슈)
    @Transactional
    public void depositBankAccount(Long accountId,
            BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        BankAccountVO bankAccountVO = getBankAccountByIdWithLock(accountId);
        bankAccountVO.deposit(bankAccountAmountRequestDto.amount());
        bankAccountMapper.updateBalance(bankAccountVO);
    }

    // 락이 걸린 getBankAccountByIdWithLock로 조회 후 수정 (동시성 이슈)
    @Transactional
    public void withdrawBankAccount(Long accountId,
            BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        BankAccountVO bankAccountVO = getBankAccountByIdWithLock(accountId);
        bankAccountVO.withdraw(bankAccountAmountRequestDto.amount());
        bankAccountMapper.updateBalance(bankAccountVO);
    }

    @Transactional
    public void softDeleteBankAccount(Long accountId) {
        BankAccountVO bankAccountVO = getBankAccountById(accountId);
        if (bankAccountVO.isDeleted()) {
            throw new HecCustomException(ErrorCode.ACCOUNT_IS_ALREADY_DELETED);
        }
        bankAccountVO.softDelete();
        bankAccountMapper.softDeleteBankAccount(bankAccountVO);
    }

    @Transactional(readOnly = true)
    public BankAccountResponseDto getBankAccount(Long accountId) {
        BankAccountVO bankAccountVO = getBankAccountById(accountId);
        UserVO userVO = userService.getUserVOById(bankAccountVO.getUserId());
        return new BankAccountResponseDto(
                accountId,
                userVO.getName(),
                bankAccountVO.getBank(),
                bankAccountVO.getAccountNumber(),
                bankAccountVO.getBalance());
    }

    @Transactional(readOnly = true)
    public List<BankAccountResponseDto> getBankAccountsByUserId(Long userId) {
        List<BankAccountWithUserNameVO> bankAccountsByUserId = bankAccountMapper.getBankAccountsByUserId(
                userId);
        return bankAccountsByUserId.stream()
                .map(bankAccountWithUserNameVO -> new BankAccountResponseDto(
                        bankAccountWithUserNameVO.getAccountId(),
                        bankAccountWithUserNameVO.getName(),
                        bankAccountWithUserNameVO.getBank(),
                        bankAccountWithUserNameVO.getAccountNumber(),
                        bankAccountWithUserNameVO.getBalance())).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<BankAccountResponseDto> getBankAccountsWithUserNameAndOptions(
            BankAccountConditionDto bankAccountConditionDto) {
        List<BankAccountWithUserNameVO> bankAccountsWithUserNameAndOptions = bankAccountMapper.getBankAccountsWithUserNameAndOptions(
                bankAccountConditionDto);
        return bankAccountsWithUserNameAndOptions.stream().map(bankAccountWithUserNameVO ->
                new BankAccountResponseDto(
                        bankAccountWithUserNameVO.getAccountId(),
                        bankAccountWithUserNameVO.getName(),
                        bankAccountWithUserNameVO.getBank(),
                        bankAccountWithUserNameVO.getAccountNumber(),
                        bankAccountWithUserNameVO.getBalance())).collect(Collectors.toList());
    }

    public boolean isBankAccountNumberExists(Long bankAccountNumber) {
        return bankAccountMapper.isBankAccountNumberExists(bankAccountNumber);
    }

    public BankAccountVO getBankAccountById(Long accountId) {
        Optional<BankAccountVO> optionalBankAccountVO = Optional.ofNullable(
                bankAccountMapper.getBankAccountById(accountId));
        return optionalBankAccountVO.orElseThrow(
                () -> new HecCustomException(ErrorCode.ACCOUNT_IS_NOT_EXIST));
    }
    public BankAccountVO getBankAccountByIdWithLock(Long accountId) {
        Optional<BankAccountVO> optionalBankAccountVO = Optional.ofNullable(
                bankAccountMapper.getBankAccountByIdWithLock(accountId));
        return optionalBankAccountVO.orElseThrow(
                () -> new HecCustomException(ErrorCode.ACCOUNT_IS_NOT_EXIST));
    }

    // 계좌를 생성할 때 계좌 번호를 생성하는 메서드
    public Long generateBankAccountNumber() {
        // UUID를 사용하여 랜덤한 고유한 계좌 번호 생성
        String bankAccountNumber = UUID.randomUUID().toString();
        // 하이픈 제거 후 숫자만 남기도록 처리 (선택적)
        bankAccountNumber = bankAccountNumber.replaceAll("-", "");
        // 원하는 길이로 잘라서 반환
        return Long.parseLong(bankAccountNumber.substring(0, 10));
    }

}
