package com.example.demo.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PlayListVideoRes {
    private long        videoId;
    private String      playListTitle;
    private String      updatedAt;
    private String      isPublic;
    private String      userName;
    private String      thumbnail;
    private String      title;
    private String      uploader;
    private String      videoUrl;
    private String      views;
    private Time        videoLength;
    private Time        playTime;
}