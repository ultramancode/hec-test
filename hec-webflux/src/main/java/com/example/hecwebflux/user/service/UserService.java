package com.example.hecwebflux.user.service;

import com.example.heccore.common.exception.ErrorCode;
import com.example.heccore.common.exception.HecCustomException;
import com.example.hecwebflux.user.dto.request.UserNameUpdateRequestDto;
import com.example.hecwebflux.user.dto.request.UserRequestDto;
import com.example.hecwebflux.user.dto.response.UserResponseDto;
import com.example.hecwebflux.user.entity.User;
import com.example.hecwebflux.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DatabaseClient databaseClient;

    public Mono<User> createUser(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto.name());
        return userRepository.save(user);
    }

    public Mono<Void> updateUserName(Long userId, UserNameUpdateRequestDto userNameUpdateRequestDto) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    user.updateUserName(userNameUpdateRequestDto.name());
                    return userRepository.save(user);
                })
                .then();
    }

    public Mono<Void> softDeleteUser(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (user.isDeleted()) {
                        return Mono.error(new HecCustomException(ErrorCode.USER_IS_ALREADY_DELETED));
                    }
                    user.softDelete();
                    return userRepository.save(user);
                })
                .then();
    }

    public Mono<UserResponseDto> getUser(Long userId) {
        return getUserById(userId)
                .map(user -> new UserResponseDto(user.getUserId(), user.getName()));
    }

    public Flux<UserResponseDto> getAllUsers() {
        return userRepository.findUsersByIsDeletedIsFalse()
                .map(user -> new UserResponseDto(user.getUserId(), user.getName()));
    }

    public Flux<UserResponseDto> getAllUsersByNativeQuery() {
        return databaseClient.sql("SELECT * FROM users WHERE is_deleted = FALSE")
                .map(row -> new UserResponseDto((Long) row.get("user_id"), (String) row.get("name")))
                .all();
    }

    public Mono<User> getUserById(Long userId) {
        return userRepository.findByUserIdAndIsDeletedIsFalse(userId)
                .switchIfEmpty(Mono.error(new HecCustomException(ErrorCode.USER_IS_NOT_EXIST)));
    }


}
