package com.example.heccore.user.model;

import com.example.heccore.common.model.BaseVO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserVO extends BaseVO {

    Long userId;
    String name;
    boolean isDeleted = false;

    public UserVO(String name) {
        this.name = name;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
    public void updateUserName(String name){
        this.name = name;
    }

    // 스프링 배치 모듈에서 테이블 컬럼명 매핑할 때 사용
    public void updateForBatchModule(Long userId, boolean isDeleted){
        this.userId = userId;
        this.isDeleted = isDeleted;
    }
}
