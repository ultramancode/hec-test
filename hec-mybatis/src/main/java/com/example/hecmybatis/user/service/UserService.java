package com.example.hecmybatis.user.service;

import com.example.heccore.common.exception.ErrorCode;
import com.example.heccore.common.exception.HecCustomException;
import com.example.heccore.user.model.UserVO;
import com.example.heccore.bank.dto.response.BankAccountResponseDto;
import com.example.hecmybatis.bankaccount.service.BankAccountService;
import com.example.heccore.user.dto.request.UserConditionDto;
import com.example.heccore.user.dto.request.UserNameUpdateRequestDto;
import com.example.heccore.user.dto.request.UserRequestDto;
import com.example.heccore.user.dto.response.UserResponseDto;
import com.example.hecmybatis.user.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BankAccountService bankAccountService;

    @Transactional
    public Long createUser(UserRequestDto userRequestDto) {
        UserVO userVO = new UserVO(userRequestDto.name());
        userMapper.createUser(userVO);
        return userVO.getUserId();
    }

    @Transactional
    public void updateUserName(Long userId, UserNameUpdateRequestDto userNameUpdateRequestDto) {
        UserVO userVO = getUserVOByIdAndDeletedIsFalse(userId);
        // 업데이트할 사용자 이름 설정
        userVO.updateUserName(userNameUpdateRequestDto.name());
        // 사용자 정보 업데이트
        userMapper.updateUserName(userVO);
    }

    @Transactional
    public void softDeleteUser(Long userId) {
        UserVO userVO = getUserVOByIdAndDeletedIsFalse(userId);
        if (userVO.isDeleted()) {
            throw new HecCustomException(ErrorCode.USER_IS_ALREADY_DELETED);
        }
        List<Long> accountIds = bankAccountService.getBankAccountsByUserIdWithDeletedIsFalse(userId)
                .stream()
                .map(BankAccountResponseDto::accountId)
                .collect(Collectors.toList());

        if(accountIds.size() != 0) {
            bankAccountService.softDeleteBankAccounts(accountIds);
        }
        userVO.softDelete();
        userMapper.softDeleteUser(userVO);
    }
    @Transactional
    public void hardDeleteUser(Long userId) {
        UserVO userVO = getUserVOById(userId);
        List<Long> accountIds = bankAccountService.getBankAccountsByUserId(userId)
                .stream()
                .map(BankAccountResponseDto::accountId)
                .collect(Collectors.toList());

        if(accountIds.size() != 0) {
            bankAccountService.hardDeleteBankAccounts(accountIds);
        }
        userVO.softDelete();
        userMapper.hardDeleteUser(userVO);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        UserVO userVO = getUserVOByIdAndDeletedIsFalse(userId);
        return new UserResponseDto(userVO.getUserId(), userVO.getName());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithOptions(UserConditionDto userConditionDto) {
        List<UserVO> userVOS = userMapper.getUsersWithOptions(userConditionDto);
        return userVOS.stream().map(userVO ->
                new UserResponseDto(userVO.getUserId(), userVO.getName())).collect(Collectors.toList());
    }

    public UserVO getUserVOByIdAndDeletedIsFalse(Long userId) {
        Optional<UserVO> userOptional = Optional.ofNullable(userMapper.getUserByIdAndDeletedIsFalse(userId));
        return userOptional.orElseThrow(() -> new HecCustomException(ErrorCode.USER_IS_NOT_EXIST));
    }

    public UserVO getUserVOById(Long userId) {
        Optional<UserVO> userOptional = Optional.ofNullable(userMapper.getUserById(userId));
        return userOptional.orElseThrow(() -> new HecCustomException(ErrorCode.USER_IS_NOT_EXIST));
    }


}
