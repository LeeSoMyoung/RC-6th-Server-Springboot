package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentProvider {
    private final CommentDao commentDao;

    @Autowired
    public CommentProvider (CommentDao commentDao){
        this.commentDao = commentDao;
    }

    @Transactional(readOnly = true)
    public List<GetCommentRes>  getCommentList(long  videoId)    throws BaseException{
        try{
            List<GetCommentRes> getCommentResList = commentDao.getCommentResList(videoId);
            return  getCommentResList;
        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetStreamingCommentRes> getStreamingComments(long   videoId)    throws BaseException{
        try{
            List<GetStreamingCommentRes> getStreamingCommentsList = commentDao.getStreamingComments(videoId);
            return  getStreamingCommentsList;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public long                         getCommentWriter(long   commentId)  throws BaseException{
        try{
            return  commentDao.getCommentWriter(commentId);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    int         checkCommentExists(long commentId)  throws BaseException{
        try{
            return commentDao.checkCommentExists(commentId);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}