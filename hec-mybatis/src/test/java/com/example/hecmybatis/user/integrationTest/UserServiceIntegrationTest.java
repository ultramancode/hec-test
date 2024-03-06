package com.example.hecmybatis.user.integrationTest;

import com.example.heccore.common.enums.Bank;
import com.example.heccore.user.model.UserVO;
import com.example.hecmybatis.bankAccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankAccount.mapper.BankAccountMapper;
import com.example.hecmybatis.bankAccount.service.BankAccountService;
import com.example.hecmybatis.user.dto.request.UserConditionDto;
import com.example.hecmybatis.user.dto.request.UserNameUpdateRequestDto;
import com.example.hecmybatis.user.dto.request.UserRequestDto;
import com.example.hecmybatis.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.mapper.UserMapper;
import com.example.hecmybatis.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.iterable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BankAccountMapper bankAccountMapper;

    @BeforeEach
    void setUp() {
        bankAccountMapper.deleteAllBankAccounts();
        userMapper.deleteAllUsers();
    }

    @Test
    @DisplayName("유저 생성 테스트")
    void createUser() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");

        // when
        Long userId = userService.createUser(userRequestDto);

        // then
        assertThat(userService.getUserVOById(userId).getName()).isEqualTo(userRequestDto.name());
    }

    @Test
    @DisplayName("유저 이름 업데이트 테스트")
    void updateUserName() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);
        UserNameUpdateRequestDto userNameUpdateRequestDto = new UserNameUpdateRequestDto("김태웅_수정");

        // when
        assertDoesNotThrow(() -> userService.updateUserName(userId, userNameUpdateRequestDto));

        // then
        assertThat(userService.getUserVOById(userId).getName()).isEqualTo(userNameUpdateRequestDto.name());
    }

    @Test
    @DisplayName("유저 소프트 딜리트 테스트")
    void softDeleteUser_Success() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);

        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(userId, Bank.SHINHAN, BigDecimal.valueOf(1));
        Long accountId = bankAccountService.createBankAccount(bankAccountRequestDto);
        // when
        assertDoesNotThrow(() -> userService.softDeleteUser(userId));

        // then
        assertThat(userMapper.getUserById(userId).isDeleted()).isSameAs(true);
        assertThat(bankAccountMapper.getBankAccountById(accountId).isDeleted()).isSameAs(true);
    }

    @Test
    @DisplayName("유저 단건 조회 테스트")
    void getUser_Success() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);

        // when
        UserResponseDto userResponseDto = userService.getUser(userId);

        // then
        assertThat(userService.getUser(userId).name()).isEqualTo(userResponseDto.name());
        assertThat(userService.getUser(userId).userId()).isEqualTo(userResponseDto.userId());

        // Additional assertions if needed
    }

    @Test
    @DisplayName("유저 다양한 조건 조회 테스트")
    void getUsersWithOptions_Success() {
        // given
        for (int i = 1; i <= 10; i++) {
            UserRequestDto userRequestDto = new UserRequestDto("김태웅"+i);
            userService.createUser(userRequestDto);
        }
        UserRequestDto userRequestDto = new UserRequestDto("박모씨");
        userService.createUser(userRequestDto);

        UserConditionDto userConditionDto = new UserConditionDto(2,3);

        // when
        List<UserResponseDto> userResponseDtos = userService.getUsersWithOptions(userConditionDto);

        // then
        // 사이즈 3일 때 2페이지의 첫번째 유저는 유저 아이디가 4여야 하니까
        assertThat(userService.getUser(4L).userId()).isEqualTo(userResponseDtos.get(0).userId());

//        //given
//        userConditionDto.setName("박");
//
//        //when
//        List<UserResponseDto> userResponseDtosSecond = userService.getUsersWithOptions(userConditionDto);
//        System.out.println(userResponseDtosSecond.size());
//        for (UserResponseDto u: userResponseDtosSecond
//             ) {
//            System.out.println(u.name());
//        }
//
//        //then
//        assertThat(userService.getUser(5L).userId()).isEqualTo(userResponseDtosSecond.get(0).userId());


        // Additional assertions if needed
    }
}


