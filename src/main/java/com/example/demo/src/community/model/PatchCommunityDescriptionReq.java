package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchCommunityDescriptionReq {
    private long    postId;
    private String  description;
}