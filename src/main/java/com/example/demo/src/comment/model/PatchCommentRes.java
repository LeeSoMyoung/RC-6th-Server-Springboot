package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchCommentRes {
    private long    commentId;
    private long    videoId;
    private long    userId;
}