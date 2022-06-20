package com.example.demo.src.videos.model;

import com.example.demo.src.comment.model.GetCommentRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetDetailVideo {
    private long        videoId;
    private long        userId;
    private String      videoUrl;
    private String      title;
    private String      description;
    private String      thumbnailUrl;
    private Timestamp createdAt;
    private Timestamp   updatedAt;
    private String     isStreamed;
    private String      streaming;
    private String      isOriginal;
    private String      isShorts;
    private String      isFollowerHided;
    private Time        playTime;
    private Time        videoLength;
    private String      view;
    private List<GetCommentRes> comments;
}