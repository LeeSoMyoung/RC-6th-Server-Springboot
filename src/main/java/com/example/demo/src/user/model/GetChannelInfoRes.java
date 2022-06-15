package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChannelInfoRes {
    private long    playListId;
    private String  profilePic;
    private String  userName;
    private int     followers;
    private String  playListTitles;
}