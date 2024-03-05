//package com.example.hecmybatis.serviceTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import com.example.hecmybatis.user.dto.response.UserResponseDto;
//import com.example.hecmybatis.user.service.UserService;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//
//@SpringBootTest
//public class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//    @Test
//    @Rollback(value = false)
//    public void testSelectAllUsers() {
//        // 모든 사용자 조회
//        List<UserResponseDto> userResponseDtos = userService.getAllUsers();
//
//        // 조회된 사용자 리스트가 null이 아니어야 함
//        assertNotNull(userResponseDtos);
//
//        // 예상한 사용자 수가 조회된 사용자 수와 동일해야 함
//        assertEquals(10, userResponseDtos.size());
//
//        // 각 사용자의 정보 확인
//        UserResponseDto user1 = userResponseDtos.get(0);
//        assertEquals("김태웅", user1.name());
//
//        UserResponseDto user2 = userResponseDtos.get(1);
//        assertEquals("이태웅", user2.name());
//
//        // 중간 생략
//
//        UserResponseDto user10 = userResponseDtos.get(9);
//        assertEquals("이정웅", user10.name());
//    }
//}
