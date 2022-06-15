package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSubscriptionRes {
    private long    subscriptionId;
    private long    userId;
    private long    channelId;
}