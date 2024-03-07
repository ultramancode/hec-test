package com.example.hecmybatis.bankAccount.unitTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.heccore.common.enums.Bank;
import com.example.hecmybatis.bankAccount.controller.BankAccountController;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountAmountRequestDto;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountConditionDto;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankAccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankAccount.service.BankAccountService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BankAccountController.class)
public class BankAccountControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @Test
    @DisplayName("계좌 생성 단위 테스트")
    public void createBankAccount() throws Exception {
        //given
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(1L, Bank.KB, 1000);

        //when, then
        mockMvc.perform(post("/api/v1/account-banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"bank\": \"KB\", \"balance\": 1000}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("계좌 생성"));

        //then
        verify(bankAccountService, times(1)).createBankAccount(bankAccountRequestDto);
    }

    @Test
    @DisplayName("계좌 소프트 딜리트 단위 테스트")
    public void softDeleteBankAccount() throws Exception {
        //given
        Long accountId = 1L;

        //when, then
        mockMvc.perform(put("/api/v1/account-banks/{accountId}/soft-delete", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("계좌 소프트 딜리트"));

        //then
        verify(bankAccountService, times(1)).softDeleteBankAccount(accountId);
    }

    @Test
    @DisplayName("계좌 입금 단위 테스트")
    public void depositBankAccount() throws Exception {
        //given
        Long accountId = 1L;
        BankAccountAmountRequestDto requestDto = new BankAccountAmountRequestDto(1000);

        //when, then
        mockMvc.perform(put("/api/v1/account-banks/{accountId}/deposit", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"amount\": 1000 }"))
                .andExpect(status().isOk())
                .andExpect(content().string("입금 성공"));

        //then
        verify(bankAccountService, times(1)).depositBankAccount(accountId, requestDto);
    }

    @Test
    @DisplayName("계좌 출금 단위 테스트")
    public void withdrawBankAccount() throws Exception {
        //given
        Long accountId = 1L;
        BankAccountAmountRequestDto requestDto = new BankAccountAmountRequestDto(500);

        //when, then
        mockMvc.perform(put("/api/v1/account-banks/{accountId}/withdraw", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"amount\": 500 }"))
                .andExpect(status().isOk())
                .andExpect(content().string("출금 성공"));

        //then
        verify(bankAccountService, times(1)).withdrawBankAccount(accountId, requestDto);
    }

    @Test
    @DisplayName("계좌 조회 단위 테스트")
    public void getBankAccount() throws Exception {
        //given
        Long accountId = 1L;
        BankAccountResponseDto bankAccountResponseDto = new BankAccountResponseDto(accountId, "김태웅",
                Bank.KB,
                1L, 1000);
        when(bankAccountService.getBankAccount(accountId)).thenReturn(bankAccountResponseDto);

        //when, then
        mockMvc.perform(get("/api/v1/account-banks/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.name").value(bankAccountResponseDto.name()))
                .andExpect(jsonPath("$.bank").value(bankAccountResponseDto.bank().toString()))
                .andExpect(
                        jsonPath("$.accountNumber").value(bankAccountResponseDto.accountNumber()))
                .andExpect(jsonPath("$.balance").value(bankAccountResponseDto.balance()));
        //then
        verify(bankAccountService, times(1)).getBankAccount(accountId);
    }

    @Test
    @DisplayName("사용자 계좌 조회 단위 테스트")
    public void getBankAccountsByUserId() throws Exception {
        //given
        Long userId = 1L;
        List<BankAccountResponseDto> bankAccountResponseDtos = Arrays.asList(
                new BankAccountResponseDto(1L, "김태웅", Bank.KB, 1L, 1000),
                new BankAccountResponseDto(2L, "김태웅", Bank.SHINHAN, 2L, 2000)
        );

        when(bankAccountService.getBankAccountsByUserId(userId)).thenReturn(
                bankAccountResponseDtos);

        //when, then
        mockMvc.perform(get("/api/v1/account-banks/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(
                        bankAccountResponseDtos.get(0).accountId()))
                .andExpect(jsonPath("$[0].name").value(bankAccountResponseDtos.get(0).name()))
                .andExpect(jsonPath("$[0].bank").value(
                        bankAccountResponseDtos.get(0).bank().toString()))
                .andExpect(jsonPath("$[0].accountNumber").value(
                        bankAccountResponseDtos.get(0).accountNumber()))
                .andExpect(jsonPath("$[0].balance").value(bankAccountResponseDtos.get(0).balance()))
                .andExpect(jsonPath("$[1].accountId").value(
                        bankAccountResponseDtos.get(1).accountId()))
                .andExpect(jsonPath("$[1].name").value(bankAccountResponseDtos.get(1).name()))
                .andExpect(jsonPath("$[1].bank").value(
                        bankAccountResponseDtos.get(1).bank().toString()))
                .andExpect(jsonPath("$[1].accountNumber").value(
                        bankAccountResponseDtos.get(1).accountNumber()))
                .andExpect(
                        jsonPath("$[1].balance").value(bankAccountResponseDtos.get(1).balance()));
        //then
        verify(bankAccountService, times(1)).getBankAccountsByUserId(userId);
    }

    @Test
    @DisplayName("계좌 검색 단위 테스트")
    public void getUBankAccountWithOptions() throws Exception {

        //given
        int page = 1;
        int size = 3;

        List<BankAccountResponseDto> bankAccountResponseDtos = Arrays.asList(
                new BankAccountResponseDto(1L, "김태웅", Bank.KB, 1L, 1000),
                new BankAccountResponseDto(2L, "김태웅", Bank.SHINHAN, 2L, 2000)
        );

        when(bankAccountService.getBankAccountsWithUserNameAndOptions(
                new BankAccountConditionDto(page, size))).thenReturn(bankAccountResponseDtos);

        //when, then
        mockMvc.perform(get("/api/v1/account-banks/search")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
    }
}