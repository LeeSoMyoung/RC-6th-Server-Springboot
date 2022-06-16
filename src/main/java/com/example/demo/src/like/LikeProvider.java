package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.like.model.PostCommentLikeReq;
import com.example.demo.src.like.model.PostVideoLikeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LikeProvider {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final   LikeDao     likeDao;

    public LikeProvider(LikeDao likeDao){
        this.likeDao = likeDao;
    }

    public int  checkVideoLike(PostVideoLikeReq postVideoLikeReq)   throws BaseException{
        try{
            return likeDao.checkVideoLike(postVideoLikeReq);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int  checkCommentLike(PostCommentLikeReq postCommentLikeReq) throws BaseException{
        try{
            return likeDao.checkCommentLike(postCommentLikeReq);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int      checkExistingUser(long  userId) throws BaseException{
        try{
            return  likeDao.isExistingUser(userId);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
    }
}