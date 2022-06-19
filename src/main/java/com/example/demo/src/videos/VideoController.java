package com.example.demo.src.videos;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.videos.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
@AllArgsConstructor
public class VideoController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final JwtService        jwtService;
    @Autowired
    private final VideoService      videoService;
    @Autowired
    private final VideoProvider     videoProvider;

    @ResponseBody
    @GetMapping("/{videoId}")
    public BaseResponse<GetVideoRes>    getVideo(@PathVariable("videoId")String  id){
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }
            long    videoId = Long.parseLong(id);
            GetVideoRes getVideoRes = videoProvider.getVideo(videoId);
            return  new BaseResponse<GetVideoRes>(getVideoRes);
        } catch(BaseException baseException){
            return new BaseResponse<>((baseException.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetVideoRes>>  getVideos(){
        // 메인 화면
        try {
            List<GetVideoRes> getVideoRes = videoProvider.getVideos();
            return new BaseResponse<>(getVideoRes);
        }catch(BaseException e){
           return new BaseResponse<>((e.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/shorts/{videoId}")
    public BaseResponse<List<GetShortsRes>>    getShortsVideo(@PathVariable("videoId")String id){
        try {
            if(!ValidationRegex.isDigit(id)){
                return  new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long    videoId = Long.parseLong(id);
            List<GetShortsRes> getShortsRes = videoProvider.getShorts(videoId);
            return new BaseResponse<List<GetShortsRes>>(getShortsRes);
        }catch(BaseException e){
            return  new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/streaming/{videoId}")
    public BaseResponse<List<GetStreamingRes>>    getStreamingVideo(@PathVariable("videoId")String id){
        try {
            if(!ValidationRegex.isDigit(id)){
                return  new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        videoId = Long.parseLong(id);
            List<GetStreamingRes> getStreamingVideo = videoProvider.getStreamingRes(videoId);
            return new BaseResponse<List<GetStreamingRes>>(getStreamingVideo);
        }catch (BaseException e){
            return new  BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/likes/{userId}")
    public BaseResponse<List<GetLikedVideoRes>>     getLikedVideos(@PathVariable("userId")String id){
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        userId = Long.parseLong(id);

            List<GetLikedVideoRes> likedVideoResList = videoProvider.getUserLikeVideos(userId);
            return new BaseResponse<>(likedVideoResList);
        }catch (BaseException e){
            return  new BaseResponse<>((e.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/history/{userId}")
    public BaseResponse<List<GetHistoryVideoRes>>   getUsersHistoryList(@PathVariable("userId")String id){
        try{

            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        userId = Long.parseLong(id);
            long        jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return  new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<GetHistoryVideoRes>    userHistoryVideoList = videoProvider.getHistoryList(userId);
            return new BaseResponse<>(userHistoryVideoList);
        }
        catch (BaseException baseException){
            return  new BaseResponse<>((baseException.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/clips/{userId}")
    public BaseResponse<List<GetClipRes>>   getUserClipList(@PathVariable("userId")String id){
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        userId = Long.parseLong(id);

            List<GetClipRes> clipList = videoProvider.getUserClips(userId);
            return  new BaseResponse<List<GetClipRes>>(clipList);
        }
        catch (BaseException baseException){
            return new BaseResponse<>((baseException.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/subscription/{userId}")
    public BaseResponse<List<GetSubscriptChannelVideoRes>>  getSubscriptChannelVideoList(@PathVariable("userId")String id){
        try{

            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }
            long        userId = Long.parseLong(id);

            long        jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<GetSubscriptChannelVideoRes> videoList = videoProvider.getSubscriptChannelVideoList(userId);
            return new BaseResponse<List<GetSubscriptChannelVideoRes>>(videoList);
        } catch (BaseException baseException){
            return new BaseResponse<>((baseException.getStatus()));
        }
    }

}