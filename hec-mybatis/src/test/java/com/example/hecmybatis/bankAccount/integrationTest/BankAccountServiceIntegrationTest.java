package com.example.hecmybatis.bankAccount.integrationTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.heccore.bank.model.BankAccountVO;
import com.example.heccore.common.enums.Bank;
import com.example.heccore.user.model.UserVO;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountConditionDto;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankAccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankAccount.mapper.BankAccountMapper;
import com.example.hecmybatis.bankAccount.service.BankAccountService;
import com.example.hecmybatis.user.dto.request.UserRequestDto;
import com.example.hecmybatis.user.mapper.UserMapper;
import com.example.hecmybatis.user.service.UserService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BankAccountServiceIntegrationTest {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountMapper bankAccountMapper;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        bankAccountMapper.deleteAllBankAccounts();
        userMapper.deleteAllUsers();
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    void createBankAccount() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(userId, Bank.KB,
                1000);

        // when
        Long accountId = bankAccountService.createBankAccount(bankAccountRequestDto);

        // then
        assertThat(accountId).isEqualTo(1L);
    }


    @Test
    @DisplayName("계좌 조회 테스트")
    void getBankAccount() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);
        UserVO userVOById = userService.getUserVOById(userId);
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(userId, Bank.KB,
                1000);
        Long accountId = bankAccountService.createBankAccount(bankAccountRequestDto);
        BankAccountVO bankAccountById = bankAccountService.getBankAccountById(accountId);
        // when
        BankAccountResponseDto bankAccountResponseDto = bankAccountService.getBankAccount(
                accountId);

        // then
        assertThat(bankAccountResponseDto.accountId()).isEqualTo(bankAccountById.getUserId());
        assertThat(bankAccountResponseDto.bank()).isEqualTo(bankAccountById.getBank());
        assertThat(bankAccountResponseDto.accountNumber()).isEqualTo(
                bankAccountById.getAccountNumber());
        assertThat(bankAccountResponseDto.balance()).isEqualTo(bankAccountById.getBalance());
        assertThat(bankAccountResponseDto.name()).isEqualTo(userVOById.getName());
    }

    @Test
    @DisplayName("계좌 다양한 조건 조회 테스트(페이징)")
    void getBankAccountsWithPaging() {
        // given
        for (int i = 1; i <= 10; i++) {
            UserRequestDto userRequestDto = new UserRequestDto("김태웅" + i);
            Long userId = userService.createUser(userRequestDto);
            BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(userId, Bank.KB,
                    1000);
            bankAccountService.createBankAccount(bankAccountRequestDto);
        }

        BankAccountConditionDto bankAccountConditionDto = new BankAccountConditionDto(2, 3);

        // when
        List<BankAccountResponseDto> bankAccountResponseDtos = bankAccountService.getBankAccountsWithUserNameAndOptions(
                bankAccountConditionDto);

        // then
        // 사이즈 3일 때 2페이지의 첫번째 계좌는 아이디가 4여야 하니까
        Assertions.assertThat(bankAccountService.getBankAccount(4L).accountId())
                .isEqualTo(bankAccountResponseDtos.get(0).accountId());
    }

    @Test
    @DisplayName("계좌 다양한 조건 조회 테스트(검색)")
    void getBankAccountsWithOptions() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);
        BankAccountRequestDto bankAccountRequestDto1 = new BankAccountRequestDto(userId, Bank.KB,
                1000);
        BankAccountRequestDto bankAccountRequestDto2 = new BankAccountRequestDto(userId,
                Bank.SHINHAN, 2000);
        bankAccountService.createBankAccount(bankAccountRequestDto1);
        bankAccountService.createBankAccount(bankAccountRequestDto2);

        BankAccountConditionDto bankAccountConditionDto = new BankAccountConditionDto(1, 3);
        bankAccountConditionDto.setBank(Bank.KB);

        // when
        List<BankAccountResponseDto> result = bankAccountService.getBankAccountsWithUserNameAndOptions(
                bankAccountConditionDto);

        // then
        assertEquals(1, result.size());
        assertThat(result.get(0).bank()).isEqualTo(Bank.KB);
    }

    @Test
    @DisplayName("계좌 다양한 조건 조회 테스트(정렬)")
    void getBankAccountsWithOrderBy() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);
        BankAccountRequestDto bankAccountRequestDto1 = new BankAccountRequestDto(userId, Bank.KB,
                1000);
        BankAccountRequestDto bankAccountRequestDto2 = new BankAccountRequestDto(userId,
                Bank.SHINHAN, 2000);
        bankAccountService.createBankAccount(bankAccountRequestDto1);
        bankAccountService.createBankAccount(bankAccountRequestDto2);

        BankAccountConditionDto bankAccountConditionDto = new BankAccountConditionDto(1, 3);
        bankAccountConditionDto.setOrderBy("bank");
        bankAccountConditionDto.setOrderDirection("asc");

        // when
        List<BankAccountResponseDto> result = bankAccountService.getBankAccountsWithUserNameAndOptions(
                bankAccountConditionDto);

        // then
        // 두 개의 계좌가 존재하므로 크기는 2
        assertThat(result.size()).isEqualTo(2);
        // 은행 이름 오름차순으로 정렬했으니 KB가 첫 번째
        assertThat(result.get(0).bank()).isEqualTo(Bank.KB);
    }
}