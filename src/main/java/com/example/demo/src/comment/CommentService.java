package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentDao commentDao;
    private final CommentProvider commentProvider;

    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider){
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
    }

    public void modifyCommentDescription(PatchCommentReq patchCommentReq)   throws BaseException{
        try{
            int res = commentDao.modifyComment(patchCommentReq);
            if(res == 0){
                // 변경에 실패했다면
                throw new BaseException(BaseResponseStatus.FAIL_TO_MODIFY_COMMENT_ERROR);
            }
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public PostCommentRes createComment(PostCommentReq postCommentReq) throws BaseException{
        if(postCommentReq.getDescription().equals("") || postCommentReq.getDescription()==null){
            throw new BaseException(BaseResponseStatus.POST_COMMENT_EMPTY_DESCRIPTION);
        }
        try{
            long    commentId = commentDao.createComment(postCommentReq);
            return  new PostCommentRes(commentId, postCommentReq.getVideoId());
        }
        catch(Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}