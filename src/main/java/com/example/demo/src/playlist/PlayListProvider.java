package com.example.demo.src.playlist;

import com.example.demo.config.BaseException;
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
        try{
            List<GetPlayListsRes>   userPlayLists = playListDao.getPlayListsByUserId(userId);
            return userPlayLists;
        }
        catch(Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<PlayListVideoRes>   getPlayListVideos(long  playListId) throws BaseException{
       try{
            List<PlayListVideoRes>  videoList = playListDao.playListVideos(playListId);
            return  videoList;
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
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