package com.example.hecmybatis.user.mapper;

import com.example.heccore.user.model.UserVO;
import java.util.List;

import com.example.hecmybatis.user.dto.request.UserConditionDto;
import com.example.hecmybatis.user.dto.request.UserRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    void createUser(UserVO userVO);
    void updateUserName(UserVO userVO);
    void softDeleteUser(UserVO userVO);
    UserVO getUserById(Long userId);

    List<UserVO> getUsersWithOptions(UserConditionDto userConditionDtoer);

}
