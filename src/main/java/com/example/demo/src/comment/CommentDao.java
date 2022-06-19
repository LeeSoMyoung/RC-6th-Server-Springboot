package com.example.demo.src.comment;

import com.example.demo.src.comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetCommentRes> getCommentResList(long  videoId){
        String  getVideoCommentQuery = "SELECT\n" +
                "    profilePicUrl       AS 'user profile picture',\n" +
                "    videoUrl            AS 'video',\n" +
                "    userName            AS 'comment username',\n" +
                "    case when TIMESTAMPDIFF(SECOND, C.createdAt,CURRENT_TIMESTAMP)<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, C.createdAt,CURRENT_TIMESTAMP),'초 전')\n" +
                "            when TIMESTAMPDIFF(MINUTE , C.createdAt,CURRENT_TIMESTAMP)<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE ,C.createdAt,CURRENT_TIMESTAMP),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR , C.createdAt,CURRENT_TIMESTAMP)<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR , C.createdAt,CURRENT_TIMESTAMP),'시간 전')\n" +
                "            when TIMESTAMPDIFF(DAY , C.createdAt,CURRENT_TIMESTAMP)<30\n" +
                "            then concat(TIMESTAMPDIFF(DAY , C.createdAt,CURRENT_TIMESTAMP),'일 전')\n" +
                "            when TIMESTAMPDIFF(MONTH ,C.createdAt,CURRENT_TIMESTAMP) < 12\n" +
                "            then concat(TIMESTAMPDIFF(MONTH ,C.createdAt,CURRENT_TIMESTAMP), '달 전')\n" +
                "            else concat(TIMESTAMPDIFF(YEAR,C.createdAt,CURRENT_TIMESTAMP), '년 전')\n" +
                "        end             AS 'comment created at',\n" +
                "        C.description       AS 'comment description',\n" +
                "        (select count(*) from Comments CC where CC.rootComment = C.commentId)   AS 'recomment number',\n" +
                "        case when TIMESTAMPDIFF(SECOND , C.createdAt, C.updatedAt) = 0 then 'not modified'\n" +
                "        else 'modified' end     AS 'is modified'\n" +
                "FROM\n" +
                "    (Comments C inner join Users U on C.userId = U.userId)\n" +
                "    inner join Videos V on V.videoId = C.videoId  and C.rootComment = 0\n" +
                "WHERE C.videoId = ?;";
        long    getVideoCommentQueryParams = videoId;

        return  this.jdbcTemplate.query(
          getVideoCommentQuery,
                (rs,rowNum) ->
                    new GetCommentRes(
                        rs.getString("user profile picture"),
                            rs.getString("video"),
                            rs.getString("comment username"),
                            rs.getString("comment created at"),
                            rs.getString("comment description"),
                            rs.getInt("recomment number"),
                            rs.getString("is modified")
                    )
                ,
                getVideoCommentQueryParams
        );
    }

    public int  modifyComment(PatchCommentReq patchCommentReq){
        String      modifyCommentQuery = "UPDATE Comments SET Description = ? WHERE commentId = ?";
        Object[]    modifyCommentQueryParams = new Object[]{patchCommentReq.getDescription(),patchCommentReq.getCommentId()};

        return this.jdbcTemplate.update(modifyCommentQuery, modifyCommentQueryParams);
    }

    public long  createComment(PostCommentReq postCommentReq){
        String      createCommentQuery = "INSERT INTO Comments(userId, rootComment, description, videoId)\n" +
                "VALUES (?, ?, ?,?);";
        Object[]    createdCommentQueryParams = new Object[]{postCommentReq.getUserId(), postCommentReq.getRootComment(), postCommentReq.getDescription(), postCommentReq.getVideoId()};
        this.jdbcTemplate.update(createCommentQuery,createdCommentQueryParams);

        String      lastInsertCommentIdQuery = "SELECT last_insert_id()";

        return  this.jdbcTemplate.queryForObject(lastInsertCommentIdQuery, long.class);
    }

    public List<GetStreamingCommentRes> getStreamingComments(long   videoId){
        String      getStreamingCommentsQuery = "SELECT\n" +
                "    V.videoId       AS 'Streaming Video PK',\n" +
                "    Super.price     AS 'Superchatting price',\n" +
                "    Super.message   AS 'Superchatting message',\n" +
                "    U.userId        AS 'Comment user PK',\n" +
                "    U.profilePicUrl AS 'Comment User Profile picture',\n" +
                "    U.userName      AS 'Comment User name',\n" +
                "    SC.description  AS 'Comment description',\n" +
                "    SC.time         AS 'Comment time'\n" +
                "FROM\n" +
                "    ((Videos V inner join StreamingComments SC on SC.videoId = V.videoId)\n" +
                "    inner join Users U on SC.userId = U.userId)\n" +
                "    left join SuperChatting Super on Super.channelId = V.videoId\n" +
                "WHERE\n" +
                "    V.videoId = ?;";
        long        getStreamingCommentsQueryParams = videoId;

        return  this.jdbcTemplate.query(
                getStreamingCommentsQuery,
                (rs, rowNum) -> new GetStreamingCommentRes(
                        rs.getLong("Streaming Video PK"),
                        rs.getInt("Superchatting price"),
                        rs.getString("Superchatting message"),
                        rs.getLong("Comment user PK"),
                        rs.getString("Comment User Profile picture"),
                        rs.getString("Comment User name"),
                        rs.getString("Comment description"),
                        rs.getTime("Comment time")
                )
                ,
                getStreamingCommentsQueryParams
        );
    }

    long        getCommentWriter(long   commentId){
        String  getCommentWriterQuery = "SELECT" +
                "   userId" +
                "FROM Comments" +
                "WHERE commentId = ?";
        long    getCommentWriterQueryParams = commentId;

        return this.jdbcTemplate.queryForObject(getCommentWriterQuery,
                long.class,
                getCommentWriterQueryParams);
    }

    int         checkCommentExists(long     commentId){
        String  commentExistQuery = "SELECT" +
                "EXISTS(SELECT commentId FROM Comments WHERE commentId = ?)";
        long    commentExistQueryParams = commentId;

        return  this.jdbcTemplate.queryForObject(
                commentExistQuery,
                int.class,
                commentExistQueryParams
        );
    }
}