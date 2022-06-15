package com.example.demo.src.view.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostVideoViewReq {
    private long    videoId;
    private long    userId;
}