package com.example.demo.src.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PlayList {
    private long        playListId;
    private long        userId;
    private String      isPublic;
    private String      isDeleted;
    private String      playListTitle;
    private Timestamp   createdAt;
    private Timestamp   updatedAt;
}