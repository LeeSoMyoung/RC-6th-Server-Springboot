package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.like.model.PostCommentLikeReq;
import com.example.demo.src.like.model.PostVideoLikeReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final   LikeProvider        likeProvider;
    @Autowired
    private final   LikeService         likeService;
    private final JwtService            jwtService;

    public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService){
        this.likeProvider = likeProvider;
        this.likeService = likeService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/{videoId}/{userId}")
    public BaseResponse<String> createVideoLike(@PathVariable("videoId") long videoId,
                                                @PathVariable("userId") long userId,
                                                @RequestBody String isLike)
    {
        try{
            if(likeProvider.checkExistingUser(userId) == 0){
                return new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }



            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            PostVideoLikeReq postVideoLikeReq = new PostVideoLikeReq(videoId, userId, isLike);
            String result = "성공적으로 영상의 좋아요/싫어요 처리가 되었습니다.";
            likeService.createVideoLike(postVideoLikeReq);
            return  new BaseResponse<String>(result);
        }
        catch (BaseException baseException){
            return  new BaseResponse<>(baseException.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/comments/{commentId}/{userId}")
    public BaseResponse<String> createCommentLike(@PathVariable("commentId")long commentId,
                                                  @PathVariable("userId")long userId,
                                                  @RequestBody String isLike)
    {
        try{

            if(likeProvider.checkExistingUser(userId) == 0){
                return new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }

            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            PostCommentLikeReq postCommentLikeReq = new PostCommentLikeReq(commentId, userId, isLike);
            likeService.createCommentLike(postCommentLikeReq);
            String result = "성공적으로 댓글의 좋아요/싫어요 처리가 되었습니다.";
            return new BaseResponse<String>(result);
        }
        catch (BaseException baseException){
           return new BaseResponse<>(baseException.getStatus());
        }
    }

}