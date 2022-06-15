package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostVideoLikeReq {
    private long    videoId;
    private long    userId;
    private String    isLike;
}