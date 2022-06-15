package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentRes  {
    private	String		profilePicUrl;
    private	String		videoUrl;
    private	String		userName;
    private	String	createdAt;
    private	String		description;
    private	int		    recommentCount;
    private	String		status;
}