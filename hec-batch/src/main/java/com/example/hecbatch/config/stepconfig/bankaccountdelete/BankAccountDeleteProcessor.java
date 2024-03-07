package com.example.hecbatch.config.stepconfig.bankaccountdelete;

import com.example.heccore.bank.model.BankAccountVO;
import com.example.heccore.user.model.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BankAccountDeleteProcessor implements ItemProcessor<BankAccountVO, BankAccountVO> {

    @Override
    public BankAccountVO process(BankAccountVO item) throws Exception {
        log.info("받아온 BankAccountVO의 ID : {}", item.getAccountId());
        return item;
    }
}
