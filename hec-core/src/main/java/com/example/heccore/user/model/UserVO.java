package com.example.heccore.user.model;

import com.example.heccore.common.model.BaseVO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
