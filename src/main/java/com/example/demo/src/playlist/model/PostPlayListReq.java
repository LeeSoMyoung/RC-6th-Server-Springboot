package com.example.demo.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPlayListReq {
    private long    userId;
    private String  playListTitle;
}