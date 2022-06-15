package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSubscriptionReq {
    private long    userId; // 현재 유저
    private long    channelId; // 구독할 채널 유저
}