package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.like.model.PostCommentLikeReq;
import com.example.demo.src.like.model.PostVideoLikeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao       likeDao;
    private final LikeProvider  likeProvider;

    @Autowired
    public LikeService(LikeDao likeDao, LikeProvider likeProvider){
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
    }

    @Transactional
    public void createCommentLike(PostCommentLikeReq postCommentLikeReq)    throws BaseException {
        if(likeProvider.checkExistingUser(postCommentLikeReq.getUserId()) == 0){
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        if(likeProvider.checkCommentLike(postCommentLikeReq)==1){
            throw new BaseException(BaseResponseStatus.POST_LIKES_ALREAY_EXISTS);
        }
        int result = likeDao.createCommentLike(postCommentLikeReq);
        if(result == 0){
            throw new BaseException(BaseResponseStatus.POST_RESULT_NOT_EXISTS);
        }
    }

    @Transactional
    public void createVideoLike(PostVideoLikeReq postVideoLikeReq)  throws BaseException{
       try{
           if(likeProvider.checkExistingUser(postVideoLikeReq.getUserId()) == 0){
               throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
           }

           if(likeProvider.checkVideoLike(postVideoLikeReq) == 1){
               throw new BaseException(BaseResponseStatus.POST_LIKES_ALREAY_EXISTS);
           }
            int result = likeDao.createVideoLike(postVideoLikeReq);
            if(result == 0){
                throw new BaseException(BaseResponseStatus.POST_RESULT_NOT_EXISTS);
            }
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}