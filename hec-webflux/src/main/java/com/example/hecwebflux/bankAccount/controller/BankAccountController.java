package com.example.hecwebflux.bankAccount.controller;

import com.example.heccore.bank.dto.request.BankAccountAmountRequestDto;
import com.example.heccore.bank.dto.request.BankAccountConditionDto;
import com.example.heccore.bank.dto.request.BankAccountRequestDto;
import com.example.heccore.bank.dto.response.BankAccountResponseDto;
import com.example.hecwebflux.bankAccount.service.BankAccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webflux-api/v1/bank-accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;


    // 계좌 생성
    @PostMapping
    public Mono<ResponseEntity<String>> createBankAccount(
            @RequestBody BankAccountRequestDto bankAccountRequestDto) {
        return bankAccountService.createBankAccount(bankAccountRequestDto)
                .map(accountId -> ResponseEntity.status(HttpStatus.CREATED).body("계좌 생성"))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + throwable.getMessage())));
    }

    // 계좌 소프트 딜리트
    @PutMapping("/{accountId}/soft-delete")
    public Mono<ResponseEntity<String>> softDeleteBankAccount(@PathVariable Long accountId) {
        return bankAccountService.softDeleteBankAccount(accountId)
                .map(v -> ResponseEntity.ok().body("계좌 소프트 딜리트"))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + throwable.getMessage())));
    }

    // 계좌 하드 딜리트
    @DeleteMapping("/{accountId}/hard-delete")
    public Mono<ResponseEntity<String>> hardDeleteBankAccount(@PathVariable Long accountId) {
        return bankAccountService.hardDeleteBankAccount(accountId)
                .then(Mono.fromCallable(() -> ResponseEntity.ok().body("계좌 하드 딜리트")))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + throwable.getMessage())));
    }


    // 계좌 입금
    @PutMapping("/{accountId}/deposit")
    public Mono<ResponseEntity<String>> depositBankAccount(
            @PathVariable Long accountId,
            @RequestBody BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        return bankAccountService.depositBankAccount(accountId, bankAccountAmountRequestDto)
                .map(v -> ResponseEntity.ok().body("입금 성공"))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + throwable.getMessage())));
    }

    // 계좌 출금
    @PutMapping("/{accountId}/withdraw")
    public Mono<ResponseEntity<String>> withdrawBankAccount(
            @PathVariable Long accountId,
            @RequestBody BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        return bankAccountService.withdrawBankAccount(accountId, bankAccountAmountRequestDto)
                .then(Mono.fromCallable(() -> ResponseEntity.ok().body("출금 성공")))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + throwable.getMessage())));
    }

    // 계좌 단건 조회
    @GetMapping("/{accountId}")
    public Mono<ResponseEntity<BankAccountResponseDto>> getBankAccount(@PathVariable Long accountId) {
        return bankAccountService.getBankAccount(accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
