package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetShortsRes {
    private String  videoUrl;
    private String  title;
    private long    userId;
    private String  profilePicUrl;
    private int     views;
    private int     comments;
    private int     likes;
    private int     followers;
}