package com.example.demo.src.videos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public  class PatchVideoReq {
    private String  videoId;
    private String  title;
    private String  videoUrl;
    private String  thumbnail;
    private String  description;
}