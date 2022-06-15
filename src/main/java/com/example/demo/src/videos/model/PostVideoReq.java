package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor

public class    PostVideoReq {
    private long        userId;
    private String      title;
    private String      description;
    private String      thumbnailUrl;
    private String      isStreamed;
    private String      streaming;
    private String      isOriginal;
    private String      isShorts;
    private Time        videoLength;
}