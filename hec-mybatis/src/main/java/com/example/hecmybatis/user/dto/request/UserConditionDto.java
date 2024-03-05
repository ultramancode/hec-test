package com.example.hecmybatis.user.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserConditionDto {
    private String name;
    private String orderBy;
    private int page;
    private int size;
    private int offset;

    public UserConditionDto(int page, int size) {
        this.page = page;
        this.size = size;
        this.offset = (page - 1) * size;
    }

    // 기본값으로 페이지를 1, 사이즈를 3으로 설정하는 생성자
    public UserConditionDto() {
        this(1, 3);
    }

    // offset 값을 계산하여 반환하는 getter 메서드
    public int getOffset() {
        return (page - 1) * size;
    }

}
