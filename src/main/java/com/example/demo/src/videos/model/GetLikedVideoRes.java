package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetLikedVideoRes {
    private Time        playTime;
    private String      thumbnailUrl;
    private String      title;
    private String      description;
    private String      createdAt;
}