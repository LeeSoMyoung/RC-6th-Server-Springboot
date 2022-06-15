package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Video{
    @Id
    private long    videoId;
    private long    userId;

    @Column(columnDefinition = "TEXT")
    private String  videoUrl;

    private String  title;

    @Column(columnDefinition = "TEXT")
    private String  description;
    @Column(columnDefinition = "TEXT")
    private String  thumbnailUrl;

    private Timestamp   createdAt;
    private Timestamp   updatedAt;
    private String      isStreamed;
    private String      streaming;
    private String      isOriginal;
    private String      isShorts;
    private String      isFollowerHided;
    private Time        playTime;
    private Time        videoLength;
}