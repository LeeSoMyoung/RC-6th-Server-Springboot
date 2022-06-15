package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider   commentProvider;
    @Autowired
    private final JwtService        jwtService;
    @Autowired
    private final CommentService    commentService;

    public CommentController(CommentProvider commentProvider, JwtService jwtService, CommentService commentService){
        this.jwtService = jwtService;
        this.commentProvider = commentProvider;
        this.commentService = commentService;
    }

    @ResponseBody
    @GetMapping("/{videoId}")
    public BaseResponse<List<GetCommentRes>>    getVideoComments(@PathVariable("videoId")long   videoId) {
        try{
            List<GetCommentRes> getCommentRes = commentProvider.getCommentList(videoId);
            return new BaseResponse<List<GetCommentRes>>(getCommentRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{videoId}/{commentId}")
    public BaseResponse<String>    patchCommentDescription(@PathVariable("videoId")long videoId, @PathVariable("commentId")long commentId, @RequestBody String modified) {
            if(modified.equals("")|| modified == null){
                return  new BaseResponse<>(BaseResponseStatus.PATCH_COMMENT_EMPTY_DESCRIPTION);
            }
            try{
                PatchCommentReq patchCommentReq = new PatchCommentReq(commentId, videoId, modified);
                commentService.modifyCommentDescription(patchCommentReq);

                String res = "";
                return  new BaseResponse<String>(res);
            }catch (BaseException baseException){
                return  new BaseResponse<>((baseException.getStatus()));
            }
    }

    @ResponseBody
    @PostMapping("/{videoId}")
    public BaseResponse<PostCommentRes> postComment(@RequestBody PostCommentReq postCommentReq){
        if(postCommentReq.getDescription().equals("") || postCommentReq.getDescription()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_COMMENT_EMPTY_DESCRIPTION);
        }
        try{
            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != postCommentReq.getUserId()){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            PostCommentRes postCommentRes = commentService.createComment(postCommentReq);
            return  new BaseResponse<PostCommentRes>(postCommentRes);
        }
        catch (BaseException baseException){
            return  new BaseResponse<>((baseException.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/streaming/{videoId}")
    public BaseResponse<List<GetStreamingCommentRes>>   getStreamingCommentsByVideoId(@PathVariable("videoId") long videoId){
        try{
            List<GetStreamingCommentRes>    getStreamingCommentResList = commentProvider.getStreamingComments(videoId);
            return  new BaseResponse<>(getStreamingCommentResList);
        }catch (BaseException baseException){
            return new BaseResponse<>((baseException.getStatus()));
        }
    }
}