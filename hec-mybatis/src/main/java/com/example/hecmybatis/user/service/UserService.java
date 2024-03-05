package com.example.hecmybatis.user.service;

import com.example.heccore.user.model.UserVO;
import com.example.hecmybatis.user.dto.request.UserConditionDto;
import com.example.hecmybatis.user.dto.request.UserNameUpdateRequestDto;
import com.example.hecmybatis.user.dto.request.UserRequestDto;
import com.example.hecmybatis.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    @Transactional
    public void createUser(UserRequestDto userRequestDto) {
        UserVO userVO = new UserVO(userRequestDto.name());
        userMapper.createUser(userVO);
    }

    @Transactional
    public void updateUserName(Long userId, UserNameUpdateRequestDto userNameUpdateRequestDto) {
        UserVO userVO = getUserVOById(userId);

        // 사용자 정보가 null이 아닌 경우에만 이름을 업데이트
        if (userVO != null) {
            // 업데이트할 사용자 이름 설정
            userVO.updateUserName(userNameUpdateRequestDto.name());
            // 사용자 정보 업데이트
            userMapper.updateUserName(userVO);
        } else {
            // 커스텀 익셉션 추가하자
        }
    }

    @Transactional
    public void softDeleteUser(Long userId) {
        UserVO userVO = getUserVOById(userId);

        // 사용자 정보가 null이 아닌 경우에만 소프트 딜리트
        if (userVO != null) {
            userVO.softDelete();
            userMapper.softDeleteUser(userVO);
        } else {
            // 커스텀 익셉션 추가하자
        }
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        UserVO userVO = getUserVOById(userId);
        return new UserResponseDto(userVO.getUserId(), userVO.getName());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithOptions(UserConditionDto userConditionDto) {

        List<UserVO> userVOS = userMapper.getUsersWithOptions(userConditionDto);


        return userVOS.stream().map(userVO ->
                new UserResponseDto(userVO.getUserId(), userVO.getName())).collect(Collectors.toList());
    }

    public UserVO getUserVOById(Long userId) {
        return userMapper.getUserById(userId);
    }

}
