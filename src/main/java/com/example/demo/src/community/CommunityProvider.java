package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.community.model.GetCommunityPostRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityProvider {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final CommunityDao  communityDao;

    @Autowired
    public CommunityProvider(CommunityDao communityDao){
        this.communityDao = communityDao;
    }

    public List<GetCommunityPostRes>    getPostList(long    channelId)  throws BaseException{
        if(checkExistingUser(channelId) == 0){
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        List<GetCommunityPostRes>   postList = communityDao.getPostList(channelId);
        return postList;
    }

    public List<GetCommunityPostRes>  getPost(long    postId) throws BaseException{
        if(checkPost(postId) == 0){
            throw   new BaseException(BaseResponseStatus.POST_NOT_EXISTS);
        }
        List<GetCommunityPostRes> getCommunityPostRes = communityDao.getCommunityPost(postId);
        return getCommunityPostRes;
    }

    public int      checkExistingUser(long  userId) throws BaseException{
        try{
            return  communityDao.isExistingUser(userId);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
    }

    public int      checkPost(long  postId) throws BaseException{
        try{
            return communityDao.checkPosts(postId);
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}