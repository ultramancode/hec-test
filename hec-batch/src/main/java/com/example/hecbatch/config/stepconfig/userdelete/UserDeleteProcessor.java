package com.example.hecbatch.config.stepconfig.userdelete;

import com.example.heccore.user.model.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class UserDeleteProcessor implements ItemProcessor<UserVO, UserVO> {

    @Override
    public UserVO process(UserVO item) throws Exception {
        log.info("받아온 UserVO의 ID : {}", item.getUserId());
        return item;
    }
}
