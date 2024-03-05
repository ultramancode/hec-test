package com.example.hecmybatis.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.heccore.common.enums.Bank;
import com.example.hecmybatis.bankAccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankAccount.service.BankAccountService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class BankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    @Rollback(value = false)
    public void testSelectAllBankAccounts() {
        // 모든 은행 계좌 조회
        List<BankAccountResponseDto> bankAccountResponseDtos = bankAccountService.getAllBankAccountsWithUser();

        // 조회된 은행 계좌 리스트가 null이 아니어야 함
        assertNotNull(bankAccountResponseDtos);

        // 예상한 은행 계좌 수가 조회된 은행 계좌 수와 동일해야 함
        assertEquals(10, bankAccountResponseDtos.size());

        // 각 은행 계좌의 정보 확인
        BankAccountResponseDto bankAccount1 = bankAccountResponseDtos.get(0);
        assertEquals(1, bankAccount1.accountId());
        assertEquals(Bank.KB, bankAccount1.bank());

        BankAccountResponseDto bankAccount2 = bankAccountResponseDtos.get(1);
        assertEquals(2, bankAccount2.accountId());
        assertEquals(Bank.NH, bankAccount2.bank());

        // 중간 생략

        BankAccountResponseDto bankAccount10 = bankAccountResponseDtos.get(9);
        assertEquals(10, bankAccount10.accountId());
        assertEquals(Bank.NH, bankAccount10.bank());
    }
}
