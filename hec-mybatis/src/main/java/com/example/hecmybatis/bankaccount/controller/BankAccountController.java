package com.example.hecmybatis.bankaccount.controller;

import com.example.hecmybatis.bankaccount.dto.request.BankAccountAmountRequestDto;
import com.example.hecmybatis.bankaccount.dto.request.BankAccountConditionDto;
import com.example.hecmybatis.bankaccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankaccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankaccount.service.BankAccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-banks")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<String> createBankAccount(
            @RequestBody BankAccountRequestDto bankAccountRequestDto) {
        bankAccountService.createBankAccount(bankAccountRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("계좌 생성");
    }

    @PutMapping("/{accountId}/soft-delete")
    public ResponseEntity<String> softDeleteBankAccount(@PathVariable Long accountId) {
        bankAccountService.softDeleteBankAccount(accountId);
        return ResponseEntity.ok().body("계좌 소프트 딜리트");
    }
    @PutMapping("/{accountId}/deposit")
    public ResponseEntity<String> depositBankAccount(
            @PathVariable Long accountId,
            @RequestBody BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        bankAccountService.depositBankAccount(accountId, bankAccountAmountRequestDto);
        return ResponseEntity.ok().body("입금 성공");
    }

    @PutMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdrawBankAccount(
            @PathVariable Long accountId,
            @RequestBody BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        bankAccountService.withdrawBankAccount(accountId, bankAccountAmountRequestDto);
        return ResponseEntity.ok().body("출금 성공");
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccountResponseDto> getBankAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok().body(bankAccountService.getBankAccount(accountId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<BankAccountResponseDto>> getBankAccountsByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok().body(bankAccountService.getBankAccountsByUserId(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BankAccountResponseDto>> getUBankAccountWithOptions
            (@ModelAttribute BankAccountConditionDto bankAccountConditionDto) {
        return ResponseEntity.ok().body(bankAccountService.getBankAccountsWithUserNameAndOptions(
                bankAccountConditionDto));
    }

}
