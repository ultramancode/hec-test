package com.example.heccore.user.model;

import com.example.heccore.common.model.BaseVO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserVO extends BaseVO {

    String id;
    String name;
    boolean isDeleted = false;

    public UserVO(String id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
