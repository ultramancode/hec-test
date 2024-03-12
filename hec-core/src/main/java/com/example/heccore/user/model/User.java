package com.example.heccore.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
@Table("users")
@NoArgsConstructor
@Getter
public class User {
    @Id
    private Long userId;
    private String name;
    private boolean isDeleted;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    public User(String name) {
        this.name = name;
        this.isDeleted = false;
    }
    public void softDelete() {
        this.isDeleted = true;
    }
    public void updateUserName(String name){
        this.name = name;
    }

}
