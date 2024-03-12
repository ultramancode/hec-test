package com.example.hecwebflux.bankAccount.service;

import com.example.heccore.bank.dto.request.BankAccountAmountRequestDto;
import com.example.heccore.bank.dto.request.BankAccountRequestDto;
import com.example.heccore.bank.dto.response.BankAccountResponseDto;
import com.example.heccore.bank.model.BankAccount;
import com.example.heccore.common.exception.ErrorCode;
import com.example.heccore.common.exception.HecCustomException;
import com.example.hecwebflux.bankAccount.repository.BankAccountRepository;
import com.example.hecwebflux.user.repository.UserRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<Long> createBankAccount(BankAccountRequestDto bankAccountRequestDto) {
        return userRepository.existsById(bankAccountRequestDto.userId())
                .flatMap(userExists -> {
                    if (!userExists) {
                        return Mono.error(new HecCustomException(ErrorCode.USER_IS_NOT_EXIST));
                    }
                    // userId가 존재하면 계좌를 생성합니다.
                    return bankAccountRepository.save(
                                    new BankAccount(
                                            bankAccountRequestDto.userId(),
                                            bankAccountRequestDto.bank(),
                                            generateBankAccountNumber(),
                                            bankAccountRequestDto.balance()
                                    ))
                            .map(BankAccount::getAccountId)
                            .as(transactionalOperator::transactional);
                });
    }

    public Mono<Void> depositBankAccount(Long accountId, BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        return getBankAccountIfExists(accountId)
                .flatMap(account -> {
                    account.deposit(bankAccountAmountRequestDto.amount());
                    return bankAccountRepository.save(account)
                            .then()
                            .as(transactionalOperator::transactional);
                });
    }

    public Mono<Void> withdrawBankAccount(Long accountId, BankAccountAmountRequestDto bankAccountAmountRequestDto) {
        return getBankAccountIfExists(accountId)
                .flatMap(account -> {
                    account.withdraw(bankAccountAmountRequestDto.amount());
                    return bankAccountRepository.save(account)
                            .then()
                            .as(transactionalOperator::transactional);
                });
    }

    public Mono<Void> softDeleteBankAccount(Long accountId) {
        return getBankAccountIfExists(accountId)
                .flatMap(account -> {
                    account.softDelete();
                    return bankAccountRepository.save(account)
                            .then()
                            .as(transactionalOperator::transactional);
                });
    }

    public Mono<Void> hardDeleteBankAccount(Long accountId){
        return getBankAccountIfExists(accountId)
                .flatMap(account -> bankAccountRepository.delete(account).then())
                .switchIfEmpty(Mono.error(new HecCustomException(ErrorCode.ACCOUNT_IS_NOT_EXIST)));
    }
    public Flux<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public Flux<BankAccount> getBankAccountsByUserId(Long userId) {
        return bankAccountRepository.findByUserIdAndIsDeletedIsFalse(userId);
    }


    private Long generateBankAccountNumber() {
        Random random = new Random();
        StringBuilder bankAccountNumberBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            bankAccountNumberBuilder.append(random.nextInt(10));
        }
        return Long.parseLong(bankAccountNumberBuilder.toString());
    }

    private Mono<BankAccount> getBankAccountIfExists(Long accountId) {
        return bankAccountRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .switchIfEmpty(Mono.error(new HecCustomException(ErrorCode.ACCOUNT_IS_NOT_EXIST)));
    }
    public Mono<BankAccountResponseDto> getBankAccount(Long accountId) {
        return bankAccountRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .switchIfEmpty(Mono.error(new HecCustomException(ErrorCode.ACCOUNT_IS_NOT_EXIST)))
                .flatMap(bankAccount -> {
                    return userRepository.findById(bankAccount.getUserId())
                            .switchIfEmpty(Mono.error(new HecCustomException(ErrorCode.USER_IS_NOT_EXIST)))
                            .map(user -> new BankAccountResponseDto(
                            bankAccount.getAccountId(),
                            user.getName(),
                            bankAccount.getBank(),
                            bankAccount.getAccountNumber(),
                            bankAccount.getBalance()
                    ));
                });
    }
}
