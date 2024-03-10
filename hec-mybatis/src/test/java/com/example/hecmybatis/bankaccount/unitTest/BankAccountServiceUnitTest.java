package com.example.hecmybatis.bankaccount.unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.heccore.bank.model.BankAccountVO;
import com.example.heccore.bank.model.BankAccountWithUserNameVO;
import com.example.heccore.common.enums.Bank;
import com.example.hecmybatis.bankaccount.dto.request.BankAccountAmountRequestDto;
import com.example.hecmybatis.bankaccount.dto.request.BankAccountConditionDto;
import com.example.hecmybatis.bankaccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankaccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankaccount.mapper.BankAccountMapper;
import com.example.hecmybatis.bankaccount.service.BankAccountService;
import java.util.ArrayList;
import java.util.List;

import com.example.hecmybatis.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BankAccountServiceUnitTest {

    @Mock
    private BankAccountMapper bankAccountMapper;


    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    public void createBankAccount() {
        // given
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(1L, Bank.KB, 1000);

        when(bankAccountMapper.isBankAccountNumberExists(any(Long.class))).thenReturn(false);
        when(userMapper.isUserExists(any(Long.class))).thenReturn(true);

        // when
        Long accountId = bankAccountService.createBankAccount(bankAccountRequestDto);

        // then
        verify(bankAccountMapper, times(1)).isBankAccountNumberExists(any(Long.class));
        verify(bankAccountMapper, times(1)).createBankAccount(any());
    }

    @Test
    @DisplayName("입금 테스트")
    public void depositBankAccount() {
        // given
        BankAccountAmountRequestDto bankAccountAmountRequestDto = new BankAccountAmountRequestDto(
                100);

        when(bankAccountMapper.getBankAccountByIdAndDeletedIsFalseWithLock(any(Long.class))).thenReturn(
                new BankAccountVO());

        // when
        bankAccountService.depositBankAccount(1L, bankAccountAmountRequestDto);

        // then
        verify(bankAccountMapper, times(1)).getBankAccountByIdAndDeletedIsFalseWithLock(any(Long.class));
        verify(bankAccountMapper, times(1)).updateBalance(any(BankAccountVO.class));
    }

    @Test
    @DisplayName("출금 테스트")
    public void withdrawBankAccount() {
        // given
        BankAccountAmountRequestDto bankAccountAmountRequestDto = new BankAccountAmountRequestDto(
                100);

        when(bankAccountMapper.getBankAccountByIdAndDeletedIsFalseWithLock(any(Long.class))).thenReturn(
                new BankAccountVO());

        // when
        bankAccountService.withdrawBankAccount(1L, bankAccountAmountRequestDto);

        // then
        verify(bankAccountMapper, times(1)).getBankAccountByIdAndDeletedIsFalseWithLock(any(Long.class));
        verify(bankAccountMapper, times(1)).updateBalance(any(BankAccountVO.class));
    }

    @Test
    @DisplayName("소프트 삭제 테스트")
    public void softDeleteBankAccount() {
        // given
        BankAccountVO bankAccountVO = new BankAccountVO(1L, Bank.KB, 123456789L, 1000);

        when(bankAccountMapper.getBankAccountByIdAndDeletedIsFalse(any(Long.class))).thenReturn(bankAccountVO);

        // when
        bankAccountService.softDeleteBankAccount(1L);

        // then
        assertTrue(bankAccountVO.isDeleted());
        verify(bankAccountMapper, times(1)).getBankAccountByIdAndDeletedIsFalse(any(Long.class));
        verify(bankAccountMapper, times(1)).softDeleteBankAccount(any(BankAccountVO.class));
    }

    @Test
    @DisplayName("사용자별 계좌 조회 테스트")
    public void getBankAccountsByUserId() {
        // given
        List<BankAccountWithUserNameVO> bankAccountWithUserNameVOList = new ArrayList<>();
        bankAccountWithUserNameVOList.add(new BankAccountWithUserNameVO());

        when(bankAccountMapper.getBankAccountsByUserIdWithDeletedIsFalse(any(Long.class))).thenReturn(
                bankAccountWithUserNameVOList);

        // when
        List<BankAccountResponseDto> result = bankAccountService.getBankAccountsByUserIdWithDeletedIsFalse(1L);

        // then
        assertEquals(1, result.size());
        verify(bankAccountMapper, times(1)).getBankAccountsByUserIdWithDeletedIsFalse(any(Long.class));
    }

    @Test
    @DisplayName("계좌 조건 조회")
    public void getBankAccountsWithUserNameAndOptions() {
        // given
        BankAccountConditionDto bankAccountConditionDto = new BankAccountConditionDto();
        List<BankAccountWithUserNameVO> bankAccountWithUserNameVOList = List.of(
                new BankAccountWithUserNameVO());

        when(bankAccountMapper.getBankAccountsWithUserNameAndOptions(
                any(BankAccountConditionDto.class)))
                .thenReturn(bankAccountWithUserNameVOList);

        // when
        List<BankAccountResponseDto> bankAccountResponseDtos = bankAccountService.getBankAccountsWithUserNameAndOptions(
                bankAccountConditionDto);

        // then
        assertEquals(bankAccountWithUserNameVOList.size(), bankAccountResponseDtos.size());
        verify(bankAccountMapper, times(1)).getBankAccountsWithUserNameAndOptions(
                any(BankAccountConditionDto.class));

    }
}
