package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSubscriptChannelVideoRes {
    private String      channelName;
    private long        userId;
    private long        videoId;
    private String      title;
    private String      isCertified;
    private int         views;
}