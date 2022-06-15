package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class GetStreamingCommentRes {
    private long        videoId;
    private int         price;
    private String      message;
    private long        userId;
    private String      profilePicUrl;
    private String      userName;
    private String      description;
    private Time        commentTime;
}