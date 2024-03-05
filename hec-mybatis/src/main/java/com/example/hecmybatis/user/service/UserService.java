package com.example.hecmybatis.user.service;

import com.example.heccore.user.model.UserVO;
import com.example.hecmybatis.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.mapper.UserMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public List<UserResponseDto> getAllUsers() {
        List<UserVO> userVOS = userMapper.selectAllUsers();

        return userVOS.stream().map(userVO ->
                    new UserResponseDto(userVO.getUserId(), userVO.getName())).collect(Collectors.toList());
    }


    public UserVO getUserVOById(Long userId) {
        return userMapper.getUserById(userId);
    }
}
