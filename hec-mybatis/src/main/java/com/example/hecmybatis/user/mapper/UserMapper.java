package com.example.hecmybatis.user.mapper;

import com.example.heccore.user.model.UserVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    UserVO getUserById(Long userId);

    @Select("SELECT * FROM users")
    List<UserVO> selectAllUsers();
}
