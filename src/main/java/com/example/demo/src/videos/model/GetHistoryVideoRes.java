package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetHistoryVideoRes {
    private long        videoId;
    private String      isShorts;
    private String      userName;
    private String      isCertified;
    private String      videoUrl;
    private String      thumbnailUrl;
    private String      title;
    private String      createdAt;
    private String      views;
    private Time        playTime;
    private Time        videoLength;
    private String      description;
}