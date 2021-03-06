package com.example.demo.src.view;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.view.model.PostVideoViewReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ViewService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final   ViewProvider    viewProvider;
    private final   ViewDao         viewDao;

    @Autowired
    public ViewService(ViewProvider viewProvider, ViewDao viewDao){
        this.viewDao = viewDao;
        this.viewProvider = viewProvider;
    }

    @Transactional
    public void createVideoView(PostVideoViewReq postVideoViewReq)  throws BaseException {
        try{
            if(viewProvider.checkUser(postVideoViewReq.getUserId()) == 0){
                throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
            }
            if(viewProvider.checkVideo(postVideoViewReq.getVideoId()) == 0){
                throw new BaseException(BaseResponseStatus.VIDEO_NOT_EXISTS);
            }
            int result = viewDao.createVideoView(postVideoViewReq);
            if(result == 0){
                throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
            }
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}