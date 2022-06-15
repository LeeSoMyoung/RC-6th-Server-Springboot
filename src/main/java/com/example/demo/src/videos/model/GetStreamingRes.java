package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStreamingRes {
    private String  videoUrl;
    private String  title;
    private String  videoDescription;
    private String  tagDescription;
    private Long    userId;
    private String  channelProfilePic;
    private String  channelName;
    private String  streamingDate;
    private int     followers;
    private int     views;
    private int     likes;
}