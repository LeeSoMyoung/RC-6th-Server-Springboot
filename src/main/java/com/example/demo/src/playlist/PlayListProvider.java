package com.example.demo.src.playlist;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.playlist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlayListProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayListDao playListDao;

    @Autowired
    public  PlayListProvider(PlayListDao playListDao){
        this.playListDao = playListDao;
    }

    @Transactional(readOnly = true)
    public List<GetPlayListsRes>    getUserPlayLists(long   userId) throws BaseException{
        if(checkExistingUser(userId) == 0){
            throw  new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        List<GetPlayListsRes>   userPlayLists = playListDao.getPlayListsByUserId(userId);
        return userPlayLists;
    }

    @Transactional(readOnly = true)
    public List<PlayListVideoRes>   getPlayListVideos(long  playListId) throws BaseException{
        if(checkExistingPlayListId(playListId) == 0){
            throw  new BaseException(BaseResponseStatus.PLAYLIST_NOT_EXISTS);
        }
        List<PlayListVideoRes>  videoList = playListDao.playListVideos(playListId);
        return  videoList;
    }

    @Transactional(readOnly = true)
    public int      checkExistingUser(long  userId) throws BaseException{
        try{
            return  playListDao.isExistingUser(userId);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
    }

    @Transactional(readOnly = true)
    public int      checkExistingPlayListId(long  playListId) throws BaseException{
        try{
            return  playListDao.isExistingPlayList(playListId);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
    }
}