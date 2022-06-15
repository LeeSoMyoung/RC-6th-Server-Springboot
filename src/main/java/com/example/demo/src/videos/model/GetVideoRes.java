package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetVideoRes {
    private long        videoId;
    private long        userId;
    private String      videoUrl;
    private String      title;
    private String      description;
    private String      thumbnailUrl;
    private Timestamp   createdAt;
    private Timestamp   updatedAt;
    private String     isStreamed;
    private String      streaming;
    private String      isOriginal;
    private String      isShorts;
    private String      isFollowerHided;
    private Time         playTime;
    private Time        videoLength;
}