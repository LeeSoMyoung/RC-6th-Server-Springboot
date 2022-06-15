package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor

public class PostVideoRes {
    private long    videoId;
    private long    userId;
}