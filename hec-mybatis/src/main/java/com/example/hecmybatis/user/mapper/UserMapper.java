package com.example.hecmybatis.user.mapper;

import com.example.heccore.user.model.UserVO;

import java.util.List;

import com.example.heccore.user.dto.request.UserConditionDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void createUser(UserVO userVO);
    void updateUserName(UserVO userVO);
    void softDeleteUser(UserVO userVO);
    void hardDeleteUser(UserVO userVO);
    UserVO getUserByIdAndDeletedIsFalse(Long userId);
    UserVO getUserById(Long userId);
    void deleteAllUsers();
    List<UserVO> getUsersWithOptions(UserConditionDto userConditionDtoer);

    boolean isUserExists(Long userId);
}
