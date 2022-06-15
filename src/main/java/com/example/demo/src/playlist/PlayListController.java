package com.example.demo.src.playlist;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.playlist.model.GetPlayListsRes;
import com.example.demo.src.playlist.model.PlayListVideoRes;
import com.example.demo.src.playlist.model.PostPlayListReq;
import com.example.demo.src.playlist.model.PostPlayListRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlayListController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final   PlayListService     playListService;
    @Autowired
    private final   PlayListProvider    playListProvider;
    @Autowired
    private final JwtService            jwtService;

    public PlayListController(PlayListService playListService, PlayListProvider playListProvider, JwtService jwtService){
        this.playListProvider = playListProvider;
        this.playListService = playListService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/users/{userId}")
    public BaseResponse<List<GetPlayListsRes>>  getUserPlayLists(@PathVariable("userId")long userId){
        try{
            List<GetPlayListsRes>   userPlayList = playListProvider.getUserPlayLists(userId);
            return  new BaseResponse<>(userPlayList);
        }
        catch(BaseException baseException){
            return  new BaseResponse<>((baseException.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{playListId}")
    public BaseResponse<List<PlayListVideoRes>> getPlayListVideos(@PathVariable("playListId")long playListId){
        try{
            List<PlayListVideoRes>  playListVideoResList = playListProvider.getPlayListVideos(playListId);
            return new BaseResponse<>(playListVideoResList);
        }
        catch (BaseException baseException){
            return  new BaseResponse<>((baseException.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/users/{userId}")
    public BaseResponse<PostPlayListRes>    createPlayList(@PathVariable("userId")long userId,
                                                           @RequestBody(required = true) String playListTitle)
    {
        try{
            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }
            PostPlayListReq postPlayListReq = new PostPlayListReq(userId, playListTitle);
            PostPlayListRes postPlayListRes = playListService.createPlayList(postPlayListReq);
            return  new BaseResponse<PostPlayListRes>(postPlayListRes);
        }
        catch (BaseException baseException){
            return new BaseResponse<>((baseException.getStatus()));
        }
    }
}