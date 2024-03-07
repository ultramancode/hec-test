package com.example.hecmybatis.user.integrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.heccore.common.enums.Bank;
import com.example.heccore.common.exception.HecCustomException;
import com.example.hecmybatis.bankaccount.dto.request.BankAccountRequestDto;
import com.example.hecmybatis.bankaccount.mapper.BankAccountMapper;
import com.example.hecmybatis.bankaccount.service.BankAccountService;
import com.example.hecmybatis.user.dto.request.UserConditionDto;
import com.example.hecmybatis.user.dto.request.UserNameUpdateRequestDto;
import com.example.hecmybatis.user.dto.request.UserRequestDto;
import com.example.hecmybatis.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.mapper.UserMapper;
import com.example.hecmybatis.user.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
    void softDeleteUser() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅");
        Long userId = userService.createUser(userRequestDto);

        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(userId,
                Bank.SHINHAN, 1000);
        Long accountId = bankAccountService.createBankAccount(bankAccountRequestDto);
        // when
        assertDoesNotThrow(() -> userService.softDeleteUser(userId));

        // then
        assertThrows((HecCustomException.class),() -> userService.getUser(userId));
    }

    @Test
    @DisplayName("유저 단건 조회 테스트")
    void getUser() {
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
    @DisplayName("유저 다양한 조건 조회 테스트(페이징)")
    void getUsersWithPaging() {
        //페이징 테스트
        // given
        for (int i = 1; i <= 10; i++) {
            UserRequestDto userRequestDto = new UserRequestDto("김태웅" + i);
            userService.createUser(userRequestDto);
        }

        UserConditionDto userConditionDto = new UserConditionDto(2, 3);

        // when
        List<UserResponseDto> userResponseDtos = userService.getUsersWithOptions(userConditionDto);

        // then (페이징 테스트)
        // 사이즈 3일 때 2페이지의 첫번째 유저는 4번째 생성된 것이어야 하니까
        assertThat(userResponseDtos.get(0).name()).isEqualTo("김태웅4");
    }


    @Test
    @DisplayName("유저 다양한 조건 조회 테스트(검색)")
    void getUsersWithSearch() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("김박태웅");
        Long userId = userService.createUser(userRequestDto);
        UserRequestDto userRequestDtoSecond = new UserRequestDto("최이태웅");
        Long userIdSecond = userService.createUser(userRequestDtoSecond);

        UserConditionDto userConditionDto = new UserConditionDto(1, 3);
        userConditionDto.setName("이");

        //when
        List<UserResponseDto> userResponseDtosWithSearch = userService.getUsersWithOptions(
                userConditionDto);

        //then (검색 기능 테스트)
        assertThat(userService.getUser(userIdSecond).name()).isEqualTo(
                userResponseDtosWithSearch.get(0).name());
    }

    @Test
    @DisplayName("유저 다양한 조건 조회 테스트(정렬)")
    void getUsersWithOrderBy() {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("김태웅1");
        Long userId = userService.createUser(userRequestDto);
        UserRequestDto userRequestDtoSecond = new UserRequestDto("김태웅2");
        Long userIdSecond = userService.createUser(userRequestDtoSecond);

        UserConditionDto userConditionDto = new UserConditionDto(1, 3);

        userConditionDto.setOrderBy("name");
        userConditionDto.setOrderDirection("desc");

        //when
        List<UserResponseDto> userResponseDtosWithOrderBy = userService.getUsersWithOptions(
                userConditionDto);

        assertThat(userService.getUser(userIdSecond).userId()).isEqualTo(
                userResponseDtosWithOrderBy.get(0).userId());
    }
}


