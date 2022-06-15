package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentReq {
    private long        userId;
    private long        rootComment;
    private String      description;
    private long        videoId;
}