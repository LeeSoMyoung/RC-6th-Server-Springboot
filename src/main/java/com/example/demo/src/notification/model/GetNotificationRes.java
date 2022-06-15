package com.example.demo.src.notification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNotificationRes {
    private long    videoId;
    private long    userId;
    private String  userName;
    private String  profilePicUrl;
    private String  thumbnail;
    private String  description;
    private String  createdAt;
}