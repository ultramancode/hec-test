package com.example.hecmybatis.user.service;

import com.example.heccore.user.model.UserVO;
import com.example.hecmybatis.user.dto.request.UserNameUpdateRequestDto;
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

    public void updateUserName(Long userId, UserNameUpdateRequestDto userNameUpdateRequestDto) {
        UserVO user = getUserVOById(userId);

        // 사용자 정보가 null이 아닌 경우에만 이름을 업데이트
        if (user != null) {
            // 업데이트할 사용자 이름 설정
            user.updateUserName(userNameUpdateRequestDto.name());
            // 사용자 정보 업데이트
            userMapper.updateUserName(user);
        } else {
            // 커스텀 익셉션 추가하자
        }
    }
}
