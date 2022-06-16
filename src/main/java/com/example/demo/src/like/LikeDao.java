package com.example.demo.src.like;

import com.example.demo.src.like.model.PostCommentLikeReq;
import com.example.demo.src.like.model.PostVideoLikeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class LikeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public int  checkVideoLike(PostVideoLikeReq postVideoLikeReq){
        String  checkQuery = "SELECT exists(" +
                "SELECT LikesAndDislikesId " +
                "FROM LikesAndDislikes" +
                "WHERE userId = ? AND videoId = ?);";
        Object[]    checkQueryParams = new Object[]{postVideoLikeReq.getUserId(), postVideoLikeReq.getVideoId()};
        return this.jdbcTemplate.queryForObject(checkQuery,int.class, checkQueryParams);
    }

    public int  checkCommentLike(PostCommentLikeReq postCommentLikeReq){
        String  checkQuery = "SELECT exists(" +
                "SELECT LikesAndDislikesId " +
                "FROM LikesAndDislikes" +
                "WHERE userId = ? AND commentId = ?);";
        Object[]    checkQueryParams = new Object[]{postCommentLikeReq.getUserId(), postCommentLikeReq.getCommentId()};
        return this.jdbcTemplate.queryForObject(checkQuery,int.class, checkQueryParams);
    }

    public int     createVideoLike(PostVideoLikeReq postVideoLikeReq){
        String      createVideoLikeQuery = "INSERT INTO LikesAndDislikes(userId, videoId, isLike) \n" +
                "VALUES(?, ?, ?);";
        Object[]    createVideoLikeQueryParams = new Object[]{postVideoLikeReq.getUserId(), postVideoLikeReq.getVideoId(),
        postVideoLikeReq.getIsLike()};

        return this.jdbcTemplate.update(createVideoLikeQuery, createVideoLikeQueryParams);
    }

    public int     createCommentLike(PostCommentLikeReq postCommentLikeReq){
        String      createCommentLikeQuery = "INSERT INTO LikesAndDislikes(userId, commentId, isLike) \n" +
                "VALUES(?, ?, ?);";
        Object[]    createCommentLikeQueryParams = new Object[]{postCommentLikeReq.getUserId(), postCommentLikeReq.getCommentId(),
        postCommentLikeReq.getIsLike()};

        return this.jdbcTemplate.update(createCommentLikeQuery, createCommentLikeQueryParams);
    }

    public  int isExistingUser(long userId){
        String  checkExistingUserQuery = "SELECT\n" +
                "    EXISTS(SELECT * FROM Users  WHERE userId = ?);";
        long    checkExistingUserQueryParams = userId;

        return this.jdbcTemplate.queryForObject(checkExistingUserQuery, int.class,checkExistingUserQueryParams);
    }

}