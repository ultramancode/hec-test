package com.example.hecmybatis.bankAccount.controller;

import com.example.hecmybatis.bankAccount.dto.request.BankAccountConditionDto;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankAccount.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankAccount.service.BankAccountService;
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