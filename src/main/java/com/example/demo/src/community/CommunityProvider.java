package com.example.demo.src.community;

import com.example.demo.config.BaseException;
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
        try{
            List<GetCommunityPostRes>   postList = communityDao.getPostList(channelId);
            return postList;
        }catch (Exception e){
            throw   new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetCommunityPostRes>  getPost(long    postId) throws BaseException{
        try{
            List<GetCommunityPostRes> getCommunityPostRes = communityDao.getCommunityPost(postId);
            return getCommunityPostRes;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}