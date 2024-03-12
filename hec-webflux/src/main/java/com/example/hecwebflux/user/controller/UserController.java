package com.example.hecwebflux.user.controller;

import com.example.heccore.user.model.User;
import com.example.hecwebflux.user.dto.request.UserNameUpdateRequestDto;
import com.example.hecwebflux.user.dto.request.UserRequestDto;
import com.example.hecwebflux.user.dto.response.UserResponseDto;
import com.example.hecwebflux.user.repository.UserRepository;
import com.example.hecwebflux.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webflux-api/v1/users")
public class UserController {
private final UserService userService;
private final UserRepository userRepository;



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
    @GetMapping("/tt/{userId}")
    public Mono<ResponseEntity<User>> getUser22(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok().body(user));
    }

    @GetMapping
    public Flux<UserResponseDto> getUsers() {
        return userService.getAllUsers();
    }
}
