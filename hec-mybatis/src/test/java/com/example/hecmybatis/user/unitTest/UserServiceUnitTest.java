package com.example.hecmybatis.user.unitTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.heccore.common.exception.HecCustomException;
import com.example.heccore.user.model.UserVO;
import com.example.hecmybatis.bankaccount.service.BankAccountService;
import com.example.heccore.user.dto.request.UserConditionDto;
import com.example.heccore.user.dto.request.UserNameUpdateRequestDto;
import com.example.heccore.user.dto.request.UserRequestDto;
import com.example.heccore.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.mapper.UserMapper;
import com.example.hecmybatis.user.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceUnitTest {
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;
    @Mock
    private BankAccountService bankAccountService;

    // mock 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저 생성 서비스 단위 테스트")
    void createUser() {

        //given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");

        //when
        assertDoesNotThrow(() -> userService.createUser(userRequestDto));

        //then
        verify(userMapper, times(1)).createUser(any(UserVO.class));
    }


    @Test
    @DisplayName("유저 업데이트 서비스 단위 테스트 - 성공")
    void updateUserName_Success() {

        //given
        Long userId = 1L;
        UserNameUpdateRequestDto userNameUpdateRequestDto = new UserNameUpdateRequestDto("김태웅_수정");
        UserVO userVO = new UserVO("김태웅");

        when(userMapper.getUserByIdAndDeletedIsFalse(userId)).thenReturn(userVO);

        //when
        assertDoesNotThrow(() -> userService.updateUserName(userId, userNameUpdateRequestDto));

        //then
        assertEquals(userNameUpdateRequestDto.name(), userVO.getName());
        verify(userMapper, times(1)).updateUserName(any(UserVO.class));
    }

    @Test
    @DisplayName("유저 업데이트 서비스 단위 테스트 - 실패")
    void updateUserName_Failure() {

        //given
        Long userId = 1L;
        UserNameUpdateRequestDto userNameUpdateRequestDto = new UserNameUpdateRequestDto("Jane Doe");

        when(userMapper.getUserByIdAndDeletedIsFalse(userId)).thenReturn(null);

        //when, then
        assertThrows(HecCustomException.class, () -> userService.updateUserName(userId, userNameUpdateRequestDto));

        //then
        verify(userMapper, never()).updateUserName(any(UserVO.class));
    }

    @Test
    @DisplayName("유저 소프트 딜리트 서비스 단위 테스트 - 성공")
    void softDeleteUser_Success() {

        //given
        Long userId = 1L;
        UserVO userVO = new UserVO("김태웅");
        when(userMapper.getUserByIdAndDeletedIsFalse(userId)).thenReturn(userVO);

        //when
        assertDoesNotThrow(() -> userService.softDeleteUser(userId));

        //then
        assertTrue(userVO.isDeleted());
        verify(userMapper, times(1)).softDeleteUser(any(UserVO.class));
    }

    @Test
    @DisplayName("유저 소프트 딜리트 서비스 단위 테스트 - 실패")
    void softDeleteUser_Failure() {

        //given
        Long userId = 1L;
        UserVO userVO = new UserVO("김태웅");
        userVO.softDelete();
        when(userMapper.getUserByIdAndDeletedIsFalse(userId)).thenReturn(userVO);

        //when, then
        assertThrows(HecCustomException.class, () -> userService.softDeleteUser(userId));

        //then
        verify(userMapper, never()).softDeleteUser(any(UserVO.class));
    }

    @Test
    @DisplayName("유저 단건 조회 - 성공")
    void getUser_Success() {

        //given
        Long userId = 1L;
        UserVO userVO = new UserVO("김태웅");

        when(userMapper.getUserByIdAndDeletedIsFalse(userId)).thenReturn(userVO);

        //when
        UserResponseDto userResponseDto = userService.getUser(userId);

        //then
        assertNotNull(userResponseDto);
        assertEquals(userVO.getUserId(), userResponseDto.userId());
        assertEquals(userVO.getName(), userResponseDto.name());
    }

    @Test
    @DisplayName("유저 단건 조회 - 실패")
    void getUser_Failure() {

        //given
        Long userId = 1L;
        when(userMapper.getUserByIdAndDeletedIsFalse(userId)).thenReturn(null);

        //when, then
        assertThrows(HecCustomException.class, () -> userService.getUser(userId));
    }

    @Test
    @DisplayName("유저 조건 조회")
    void getUsersWithOptions_Success() {

        //given
        UserConditionDto userConditionDto = new UserConditionDto();
        List<UserVO> userVOList = Arrays.asList(new UserVO("김태웅"), new UserVO("김태웅2"), new UserVO("김태웅3"));

        when(userMapper.getUsersWithOptions(userConditionDto)).thenReturn(userVOList);

        //when
        List<UserResponseDto> userResponseDtoList = userService.getUsersWithOptions(userConditionDto);

        //then
        assertNotNull(userResponseDtoList);
        assertEquals(userVOList.size(), userResponseDtoList.size());
    }


}
