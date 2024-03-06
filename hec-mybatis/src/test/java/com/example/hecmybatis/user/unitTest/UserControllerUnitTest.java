package com.example.hecmybatis.user.unitTest;


import com.example.hecmybatis.user.controller.UserController;
import com.example.hecmybatis.user.dto.request.UserConditionDto;
import com.example.hecmybatis.user.dto.request.UserNameUpdateRequestDto;
import com.example.hecmybatis.user.dto.request.UserRequestDto;
import com.example.hecmybatis.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("유저 생성 단위 테스트")
    public void createUser_shouldReturnCreated() throws Exception {

        //given
        UserRequestDto requestDto = new UserRequestDto("김태웅");

        //when, then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"김태웅\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("유저 생성"));

        //then
        verify(userService, times(1)).createUser(requestDto);
    }

    @Test
    @DisplayName("유저 업데이트 단위 테스트")
    public void updateUser_shouldReturnOk() throws Exception {

        //given
        Long userId = 1L;
        UserNameUpdateRequestDto requestDto = new UserNameUpdateRequestDto("김태웅_수정");

        //when, then
        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"김태웅_수정\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("유저 업데이트"));

        //then
        verify(userService, times(1)).updateUserName(userId, requestDto);
    }

    @Test
    @DisplayName("유저 소프트 딜리트 단위 테스트")
    public void softDeleteUser_shouldReturnOk() throws Exception {

        //given
        Long userId = 1L;

        //when, then
        mockMvc.perform(put("/api/v1/users/{userId}/soft-delete", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("유저 소프트 딜리트"));

        //then
        verify(userService, times(1)).softDeleteUser(userId);
    }

    @Test
    @DisplayName("유저 단건 조회 단위 테스트")
    public void getUser_shouldReturnUser() throws Exception {

        //given
        Long userId = 1L;
        UserResponseDto responseDto = new UserResponseDto(userId, "김태웅");
        when(userService.getUser(userId)).thenReturn(responseDto);

        //when, then
        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));

        //then
        verify(userService, times(1)).getUser(userId);
    }

    @Test
    @DisplayName("유저 조회 단위 테스트")
    public void getUsersWithOptions_shouldReturnUsers() throws Exception {

        //given
        int page = 1;
        int size = 3;

        List<UserResponseDto> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(new UserResponseDto((long) i, "김태웅" + i));
        }

        when(userService.getUsersWithOptions(new UserConditionDto(page, size))).thenReturn(userList);

        //when, then
        mockMvc.perform(get("/api/v1/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
    }
}

