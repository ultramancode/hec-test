package com.example.heccore.user.model;

import com.example.heccore.common.model.BaseVO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
// Batch 모듈에서 RowMapper 빈을 생성하기 위해서 필요, 데이터베이스와 매핑 위함
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

}
