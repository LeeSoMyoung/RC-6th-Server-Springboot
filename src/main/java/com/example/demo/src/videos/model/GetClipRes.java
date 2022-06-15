package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetClipRes {
    private String      clipTitle;
    private String      clipUrl;
    private String      createdAt;
    private long        videoId;
    private String      userName;
    private Time        clipLength;
    private int         clipViews;
}