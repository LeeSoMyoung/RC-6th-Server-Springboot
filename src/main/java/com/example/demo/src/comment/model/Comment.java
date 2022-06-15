package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    @Id
    private long        commentId;
    private long        userId;
    private long        rootComment;
    private long        videoId;

    @Column(columnDefinition = "TEXT")
    private String      description;

    private Timestamp   createdAt;
    private Timestamp   updatedAt;
    private String      status;
}