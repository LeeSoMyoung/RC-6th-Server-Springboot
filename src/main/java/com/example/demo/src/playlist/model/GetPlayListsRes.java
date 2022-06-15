package com.example.demo.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPlayListsRes {
    private long    playListId;
    private long    userId;
    private String  playListTitle;
}