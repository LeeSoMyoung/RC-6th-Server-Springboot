package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
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
    public BaseResponse<List<GetCommentRes>>    getVideoComments(@PathVariable("videoId")String id) {
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        videoId = Long.parseLong(id);
            List<GetCommentRes> getCommentRes = commentProvider.getCommentList(videoId);
            return new BaseResponse<List<GetCommentRes>>(getCommentRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{videoId}/{commentId}")
    public BaseResponse<String>    patchCommentDescription(@PathVariable("videoId")String video, @PathVariable("commentId")String comment, @RequestBody String modified) {
            try{
                if(modified.equals("")|| modified == null){
                    return  new BaseResponse<>(BaseResponseStatus.PATCH_COMMENT_EMPTY_DESCRIPTION);
                }

                if(!ValidationRegex.isDigit(comment)){
                    return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
                }

                if(!ValidationRegex.isDigit(video)){
                    return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
                }

                long    commentId = Long.parseLong(comment);
                long    videoId = Long.parseLong(video);

                long    jwtUserId = jwtService.getUserId();
                long    userId = commentProvider.getCommentWriter(commentId);

                if(jwtUserId != userId){
                    return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
                }

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
        try{

            if(postCommentReq.getDescription().equals("") || postCommentReq.getDescription()==null){
                return new BaseResponse<>(BaseResponseStatus.POST_COMMENT_EMPTY_DESCRIPTION);
            }

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
    public BaseResponse<List<GetStreamingCommentRes>>   getStreamingCommentsByVideoId(@PathVariable("videoId") String id){
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        videoId = Long.parseLong(id);
            List<GetStreamingCommentRes>    getStreamingCommentResList = commentProvider.getStreamingComments(videoId);
            return  new BaseResponse<>(getStreamingCommentResList);
        }catch (BaseException baseException){
            return new BaseResponse<>((baseException.getStatus()));
        }
    }
}