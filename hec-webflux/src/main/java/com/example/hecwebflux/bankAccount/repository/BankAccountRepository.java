package com.example.hecwebflux.bankAccount.repository;

import com.example.heccore.bank.model.BankAccount;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountRepository extends ReactiveCrudRepository<BankAccount, Long> {
    Flux<BankAccount> findByUserIdAndIsDeletedIsFalse(Long userId);
    Mono<BankAccount> findByAccountIdAndIsDeletedIsFalse(Long id);
}
