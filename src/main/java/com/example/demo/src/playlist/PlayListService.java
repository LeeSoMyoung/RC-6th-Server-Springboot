package com.example.demo.src.playlist;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.playlist.model.PostPlayListReq;
import com.example.demo.src.playlist.model.PostPlayListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PlayListService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final PlayListDao       playListDao;
    private final PlayListProvider   playListProvider;

    @Autowired
    public PlayListService(PlayListDao playListDao, PlayListProvider playListProvider){
        this.playListDao = playListDao;
        this.playListProvider = playListProvider;
    }

    @Transactional
    public PostPlayListRes createPlayList(PostPlayListReq postPlayListReq) throws BaseException{
        try{
            if(playListProvider.checkExistingUser(postPlayListReq.getUserId()) == 0){
                throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
            }

            long    playListId = playListDao.createdPlayList(postPlayListReq);
            PostPlayListRes postPlayListRes = new PostPlayListRes(playListId, postPlayListReq.getUserId());
            return postPlayListRes;
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}