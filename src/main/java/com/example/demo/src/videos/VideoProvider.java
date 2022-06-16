package com.example.demo.src.videos;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.videos.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VideoProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VideoDao  videoDao;

    @Autowired
    public VideoProvider (VideoDao videoDao){
        this.videoDao = videoDao;
    }

    @Transactional(readOnly = true)
    public List<GetVideoRes>    getVideos() throws BaseException{
        try{
            List<GetVideoRes>   getVideoRes = videoDao.getVideos();
            return  getVideoRes;
        }
        catch (Exception exception){
            throw   new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public GetVideoRes  getVideo    (long   videoId)    throws BaseException{
        try{
            GetVideoRes getVideoRes = videoDao.getVideo(videoId);
            return getVideoRes;
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public  List<GetShortsRes>     getShorts(long videoId)    throws BaseException{
        try{
            List<GetShortsRes>    shortsVideo = videoDao.getShortsRes(videoId);
            return  shortsVideo;
        }
        catch(Exception e){
            throw   new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetStreamingRes>  getStreamingRes(long    videoId)    throws BaseException{
        try{
            List<GetStreamingRes> streamingRes = videoDao.getStreamingRes(videoId);
            return  streamingRes;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetLikedVideoRes>   getUserLikeVideos(long  userId) throws BaseException{
        try{
            List<GetLikedVideoRes>  likeVideos = videoDao.getLikeVideos(userId);
            return likeVideos;
        }
        catch(Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetHistoryVideoRes> getHistoryList(long userId) throws BaseException{
        try{
            List<GetHistoryVideoRes> historyList = videoDao.getUserHistory(userId);
            return  historyList;
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetClipRes> getUserClips(long   userId) throws BaseException{
        try{
            List<GetClipRes> clipList = videoDao.userClips(userId);
            return clipList;
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetSubscriptChannelVideoRes>    getSubscriptChannelVideoList(long   userId) throws BaseException{
        try{
            List<GetSubscriptChannelVideoRes> videoList = videoDao.getSubscriptChannelVideos(userId);
            return videoList;
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public int  checkVideo(long videoId)    throws BaseException{
        try{
            return videoDao.checkExistingVideo(videoId);
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public int  checkUser(long  userId) throws BaseException{
        try{
            return videoDao.isExistingUser(userId);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}