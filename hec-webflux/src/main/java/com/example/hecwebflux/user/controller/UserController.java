package com.example.hecwebflux.user.controller;

import com.example.hecwebflux.user.dto.request.UserNameUpdateRequestDto;
import com.example.hecwebflux.user.dto.request.UserRequestDto;
import com.example.hecwebflux.user.dto.response.UserResponseDto;
import com.example.hecwebflux.user.entity.User;
import com.example.hecwebflux.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webflux-api/v1/users")
public class UserController {
private final UserService userService;



    @PostMapping
    public Mono<ResponseEntity<String>> createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body("유저 생성"));
    }

    @PutMapping("/{userId}")
    public Mono<ResponseEntity<String>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserNameUpdateRequestDto userNameUpdateRequestDto
    ) {
        return userService.updateUserName(userId, userNameUpdateRequestDto)
                .then(Mono.fromCallable(() -> ResponseEntity.ok().body("유저 업데이트")));
    }

    @PutMapping("/{userId}/soft-delete")
    public Mono<ResponseEntity<String>> softDeleteUser(@PathVariable Long userId) {
        return userService.softDeleteUser(userId)
                .then(Mono.fromCallable(() -> ResponseEntity.ok().body("유저 소프트 딜리트")));
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserResponseDto>> getUser(@PathVariable Long userId) {
        return userService.getUser(userId)
                .map(userResponseDto -> ResponseEntity.ok().body(userResponseDto));
    }

    @GetMapping
    public Flux<UserResponseDto> getUsers() {
        return userService.getAllUsers();
    }
}
