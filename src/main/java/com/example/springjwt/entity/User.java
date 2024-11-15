package com.example.springjwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String roles; // ADMIN, USER ..
    @CreationTimestamp
    private Timestamp createDate;

    // 한 사용자에 role이 두개가 있을 경우
    public List<String> getRoleList(){
        if(this.roles.length() > 0) return Arrays.asList(this.roles.split(","));
        return new ArrayList<>();
    }
}
