package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommunityPostRes {
    private long        userId;
    private String      profilePicUrl;
    private String      isCertified;
    private String      userName;
    private String      description;
    private String      createdAt;
    private String      isModified;
    private String      contentUrl;
}