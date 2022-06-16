package com.example.demo.src.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUser {
    long    userId;
    String  email;
    String  userName;
}