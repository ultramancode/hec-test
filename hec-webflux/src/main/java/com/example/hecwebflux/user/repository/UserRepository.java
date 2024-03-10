package com.example.hecwebflux.user.repository;

import com.example.heccore.user.model.UserVO;
import com.example.hecwebflux.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByUserIdAndIsDeletedIsFalse(Long userId);

    Flux<User> findUsersByIsDeletedIsFalse();
}
