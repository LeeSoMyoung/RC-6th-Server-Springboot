package com.example.demo.src.videos;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.videos.model.PostVideoReq;
import com.example.demo.src.videos.model.PostVideoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VideoDao          videoDao;

    @Autowired
    public VideoService(VideoDao videoDao){
        this.videoDao = videoDao;
    }

}