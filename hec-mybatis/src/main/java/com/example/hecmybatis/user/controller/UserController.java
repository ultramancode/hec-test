package com.example.hecmybatis.user.controller;

import com.example.heccore.user.dto.request.UserConditionDto;
import com.example.heccore.user.dto.request.UserNameUpdateRequestDto;
import com.example.heccore.user.dto.request.UserRequestDto;
import com.example.heccore.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 생성
    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("유저 생성");
    }

    // 유저 이름 변경
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestBody UserNameUpdateRequestDto userNameUpdateRequestDto
    ) {
        userService.updateUserName(userId, userNameUpdateRequestDto);
        return ResponseEntity.ok().body("유저 업데이트");
    }

    // 유저 소프트 딜리트
    @PutMapping("/{userId}/soft-delete")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long userId) {
        userService.softDeleteUser(userId);
        return ResponseEntity.ok().body("유저 소프트 딜리트");
    }

    // 유저 하드 딜리트
    @DeleteMapping("/{userId}/hard-delete")
    public ResponseEntity<String> hardDeleteUser(@PathVariable Long userId) {
        userService.hardDeleteUser(userId);
        return ResponseEntity.ok().body("유저 하드 딜리트");
    }

    // 유저 단건 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }

    // 유저 조건 조회(검색, 정렬, 페이징)
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> getUsersWithOptions
            (@ModelAttribute UserConditionDto userConditionDto) {
        return ResponseEntity.ok().body(userService.getUsersWithOptions(userConditionDto));
    }
}
