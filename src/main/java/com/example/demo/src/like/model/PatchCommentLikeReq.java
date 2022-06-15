package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchCommentLikeReq {
    private long    userId;
    private long    commentId;
    private String    isLike;
}