package com.example.demo.src.videos;

import com.example.demo.src.comment.CommentDao;
import com.example.demo.src.videos.model.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@AllArgsConstructor
public class VideoDao{

    private JdbcTemplate jdbcTemplate;
    private final CommentDao    commentDao;
    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public List<GetVideoRes> getVideos() {
        String  getVideosQuery = "SELECT *,\n" +
                "    (SELECT\n" +
                "         CASE\n" +
                "            when view<1000  then    concat(view,'회')\n" +
                "            when view<10000 then    concat(round(view/1000,1),'천 회')\n" +
                "            when view<100000000 then concat(round(view/10000,1),'만 회')\n" +
                "            else concat(round(view/100000000,1),'억 회')\n" +
                "        END\n" +
                "     FROM\n" +
                "         (SELECT COUNT(*) AS view  FROM Views WHERE Views.videoId = Videos.videoId) v\n" +
                "    ) as 'view count'\n" +
                "FROM Videos WHERE Videos.isShorts = 'N';";
        return  this.jdbcTemplate.query(
          getVideosQuery,
                (rs, rowNum) -> new GetVideoRes(
                    rs.getLong("videoId"),
                    rs.getLong("userId"),
                    rs.getString("videoUrl"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("thumbnailUrl"),
                    rs.getTimestamp("createdAt"),
                    rs.getTimestamp("updatedAt"),
                    rs.getString("isStreamed"),
                    rs.getString("streaming"),
                    rs.getString("isOriginal"),
                    rs.getString("isShorts"),
                    rs.getString("isFollowerHided"),
                    rs.getTime("playTime"),
                    rs.getTime("videoLength"),
                        rs.getString("view count")
            )
        );
    }

    public List<GetShortsRes> getShortsRes(long  videoId){
        String  getShortsQuery = "SELECT\n" +
                "    videoUrl        AS 'Shorts Video',\n" +
                "    title           AS 'Shorts title',\n" +
                "    U.userId        AS 'Channel PK',\n" +
                "    U.profilePicUrl AS 'Channel Profile',\n" +
                "    (SELECT COUNT(*) FROM Views WHERE Views.videoId = V.videoId)    AS 'Shorts Views',\n" +
                "    (SELECT COUNT(*) FROM Comments WHERE Comments.videoId = V.videoId)  AS 'the number of comments',\n" +
                "    (SELECT COUNT(*) FROM VideoLikes VL WHERE VL.videoId = V.videoId)   AS 'the number of likes',\n" +
                "    (SELECT COUNT(*) FROM Subscriptions S WHERE S.subscriptionId = U.userId)    AS 'the number of the followers'\n" +
                "FROM\n" +
                "    (Videos V inner join Users U on V.userId = U.userId and V.isShorts = 'Y' and U.status = 'ACTIVE')\n" +
                "WHERE\n" +
                "    V.videoId = ?;";
        long    getShortsQueryParams = videoId;

        return  this.jdbcTemplate.query(
                getShortsQuery,
                (rs, rowNum) ->
                    new GetShortsRes(
                            rs.getString("Shorts Video"),
                            rs.getString("Shorts title"),
                            rs.getLong("Channel PK"),
                            rs.getString("Channel Profile"),
                            rs.getInt("Shorts Views"),
                            rs.getInt("the number of comments"),
                            rs.getInt("the number of likes"),
                            rs.getInt("the number of the followers")
                    )
                ,
                getShortsQueryParams
        );
    }

    public GetDetailVideo getVideo(long   videoId){
        String  getVideoQuery = "SELECT *,\n" +
                "    (SELECT\n" +
                "         CASE\n" +
                "            when view<1000  then    concat(view,'회')\n" +
                "            when view<10000 then    concat(round(view/1000,1),'천 회')\n" +
                "            when view<100000000 then concat(round(view/10000,1),'만 회')\n" +
                "            else concat(round(view/100000000,1),'억 회')\n" +
                "        END\n" +
                "     FROM\n" +
                "         (SELECT COUNT(*) AS view  FROM Views WHERE Views.videoId = Videos.VideoId) V\n" +
                "         ) as 'view count'\n" +
                "FROM Videos WHERE Videos.isShorts = 'N' and Videos.videoId = ?;";
        long  getVideoParmas = videoId;

        return  this.jdbcTemplate.queryForObject(
                getVideoQuery,
                (rs, rowNum) -> new GetDetailVideo
                        (
                                rs.getLong("videoId"),
                                rs.getLong("userId"),
                                rs.getString("videoUrl"),
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getString("thumbnailUrl"),
                                rs.getTimestamp("createdAt"),
                                rs.getTimestamp("updatedAt"),
                                rs.getString("isStreamed"),
                                rs.getString("streaming"),
                                rs.getString("isOriginal"),
                                rs.getString("isShorts"),
                                rs.getString("isFollowerHided"),
                                rs.getTime("playTime"),
                                rs.getTime("videoLength"),
                                rs.getString("view count"),
                                commentDao.getCommentResList(rs.getLong("videoId"))
                        ),
                getVideoParmas
        );
    }

    public List<GetStreamingRes> getStreamingRes (long   videoId){
        String  getStreamingVideoQuery = "SELECT\n" +
                "    VideoUrl        AS 'streaming video',\n" +
                "    title           AS 'streaming video title',\n" +
                "    V.description   AS 'video description',\n" +
                "    T.description   AS 'tag description',\n" +
                "    U.userId        AS 'User PK',\n" +
                "    U.profilePicUrl AS 'Channel profile picture',\n" +
                "    U.userName      AS 'Channel name',\n" +
                "    concat('스트리밍 시작일: ',DATE(V.createdAt))  AS 'Streaming date',\n" +
                "    (SELECT COUNT(*) FROM Subscriptions S WHERE S.subscriptionId = U.userId)    AS 'the number of the followers',\n" +
                "    (SELECT COUNT(*) FROM Views WHERE V.videoId = Views.videoId)    AS 'Streaming Views',\n" +
                "    (SELECT COUNT(*) FROM VideoLikes VL WHERE VL.videoId = V.videoId)   AS 'Streaming Video Views'\n" +
                "FROM\n" +
                "    (Videos V inner join Tags T on V.videoId = T.tagId)\n" +
                "    inner join Users U on U.userId = V.userId\n" +
                "WHERE\n" +
                "    V.videoId = ?\n" +
                "    and (V.isStreamed = 'Y' or V.streaming = 'Y');";
        long    getStreamingVideoQueryParams = videoId;

        return  this.jdbcTemplate.query(getStreamingVideoQuery,
                (rs, rowNum) -> new GetStreamingRes(
                           rs.getString("streaming video"),
                        rs.getString("streaming video title"),
                        rs.getString("video description"),
                        rs.getString("tag description"),
                        rs.getLong("User PK"),
                        rs.getString("Channel profile picture"),
                        rs.getString("Channel name"),
                        rs.getString("Streaming date"),
                        rs.getInt("the number of the followers"),
                        rs.getInt("Streaming Views"),
                        rs.getInt("Streaming Video Views")
                    )
                ,
                getStreamingVideoQueryParams);
    }

    public List<GetLikedVideoRes>   getLikeVideos(long  userId){
        String  getLikeVideoQuery = "SELECT\n" +
                "    playTime        AS 'videoPlayTime',\n" +
                "    thumbnailUrl    AS 'thumbnail',\n" +
                "    title           AS 'video title',\n" +
                "    V.description     AS 'video description',\n" +
                "    CASE\n" +
                "            when TIMESTAMPDIFF(SECOND ,V.createdAt,CURRENT_TIMESTAMP) < 60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND ,V.createdAt,CURRENT_TIMESTAMP), '초 전')\n" +
                "            when TIMESTAMPDIFF(MINUTE ,V.createdAt,CURRENT_TIMESTAMP) < 60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE ,V.createdAt,CURRENT_TIMESTAMP), '분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR,V.createdAt,CURRENT_TIMESTAMP) < 24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR,V.createdAt,CURRENT_TIMESTAMP), '시간 전')\n" +
                "            when TIMESTAMPDIFF(HOUR,V.createdAt,CURRENT_TIMESTAMP) < 48\n" +
                "            then '하루 전'\n" +
                "            when TIMESTAMPDIFF(DAY,V.createdAt,CURRENT_TIMESTAMP) < 30\n" +
                "            then concat(TIMESTAMPDIFF(DAY ,V.createdAt,CURRENT_TIMESTAMP), '일 전')\n" +
                "            when TIMESTAMPDIFF(MONTH ,V.createdAt,CURRENT_TIMESTAMP) < 12\n" +
                "            then concat(TIMESTAMPDIFF(MONTH ,V.createdAt,CURRENT_TIMESTAMP), '달 전')\n" +
                "            else concat(TIMESTAMPDIFF(YEAR,V.createdAt,CURRENT_TIMESTAMP), '년 전')\n" +
                "        end AS 'upload date'\n" +
                "FROM\n" +
                "    (Videos V inner join LikesAndDislikes LAD on V.videoId = LAD.videoId and LAD.isLike = 'Y')\n" +
                "    inner join Users U on U.userId = LAD.userId and U.status='ACTIVE'\n" +
                "WHERE\n" +
                "    LAD.userId = ?;";
        long    getLikeVideoQueryParams = userId;

        return this.jdbcTemplate.query(
                getLikeVideoQuery,
                (rs, rowNum) -> new GetLikedVideoRes(
                        rs.getTime("videoPlayTime"),
                        rs.getString("thumbnail"),
                        rs.getString("video title"),
                        rs.getString("video description"),
                        rs.getString("upload date")
                )
                ,
                getLikeVideoQueryParams
        );
    }

    public List<GetHistoryVideoRes> getUserHistory(long userId){
        String getUserHistoryQuery="SELECT\n" +
                "    V.videoId                                                                              AS 'Video PK',\n" +
                "    isShorts                                                                               AS 'check is Shorts',\n" +
                "    userName                                                                               AS 'video uploader',\n" +
                "    isCertified                                                                            AS 'is checked user',\n" +
                "    videoUrl                                                                               AS 'video',\n" +
                "    thumbnailUrl                                                                           AS 'video thumbnail',\n" +
                "    title                                                                                  AS 'video title',\n" +
                "    case when TIMESTAMPDIFF(SECOND, V.createdAt,CURRENT_TIMESTAMP)<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, V.createdAt,CURRENT_TIMESTAMP),'초 전')\n" +
                "            when TIMESTAMPDIFF(MINUTE , V.createdAt,CURRENT_TIMESTAMP)<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE ,V.createdAt,CURRENT_TIMESTAMP),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR , V.createdAt,CURRENT_TIMESTAMP)<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR , V.createdAt,CURRENT_TIMESTAMP),'시간 전')\n" +
                "            when TIMESTAMPDIFF(DAY , V.createdAt,CURRENT_TIMESTAMP)<30\n" +
                "            then concat(TIMESTAMPDIFF(DAY , V.createdAt,CURRENT_TIMESTAMP),'일 전')\n" +
                "            when TIMESTAMPDIFF(MONTH ,V.createdAt,CURRENT_TIMESTAMP) < 12\n" +
                "            then concat(TIMESTAMPDIFF(MONTH ,V.createdAt,CURRENT_TIMESTAMP), '달 전')\n" +
                "            else concat(TIMESTAMPDIFF(YEAR,V.createdAt,CURRENT_TIMESTAMP), '년 전')\n" +
                "        end                                                                             AS 'video uploaded at',\n" +
                "    (select\n" +
                "    CASE\n" +
                "        when views<1000\n" +
                "        then concat(views,'회')\n" +
                "        when views<10000\n" +
                "        then concat(round(views/1000,1),'천 회')\n" +
                "        when views<100000000\n" +
                "        then concat(round(views/10000,1),'만 회')\n" +
                "        else concat(round(views/100000000,1),'억 회')\n" +
                "    END\n" +
                "     from\n" +
                "         (select count(*) as views from Views where Views.videoId = V.videoId) result\n" +
                "         )\n" +
                "                                                                                       AS 'views',\n" +
                "    playTime                                                                           AS 'user playtime',\n" +
                "    V.videoLength                                                                      AS 'video length',\n" +
                "    V.description                                                                      AS 'video description'\n" +
                "FROM\n" +
                "    (History H inner join Users U on H.userId = U.userId)\n" +
                "    inner join Videos V on H.videoId = V.videoId\n" +
                "WHERE\n" +
                "    U.status = 'ACTIVE'\n" +
                "    and U.userId = ?;";
        long    getUserHistoryQueryParams = userId;

        return  this.jdbcTemplate.query(
                getUserHistoryQuery,
                (rs, rowNum) -> new GetHistoryVideoRes(
                        rs.getLong("Video PK"),
                        rs.getString("check is Shorts"),
                        rs.getString("video uploader"),
                        rs.getString("is checked user"),
                        rs.getString("video"),
                        rs.getString("video thumbnail"),
                        rs.getString("video title"),
                        rs.getString("video uploaded at"),
                        rs.getString("views"),
                        rs.getTime("user playtime"),
                        rs.getTime("video length"),
                        rs.getString("video description")
                )
                ,
                getUserHistoryQueryParams
        );
    }

    public List<GetClipRes>     userClips(long  userId){
        String  userClipsListQuery = "SELECT\n" +
                "    C.description   AS 'Clip title',\n" +
                "    clipUrl     AS 'clip url',\n" +
                "    case when TIMESTAMPDIFF(SECOND , C.createdAt, CURRENT_TIMESTAMP) < 60\n" +
                "         then concat(TIMESTAMPDIFF(SECOND , C.createdAt, CURRENT_TIMESTAMP), '초 전')\n" +
                "        when TIMESTAMPDIFF(MINUTE , C.createdAt, CURRENT_TIMESTAMP) < 60\n" +
                "        then concat(TIMESTAMPDIFF(MINUTE , C.createdAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "        when TIMESTAMPDIFF(HOUR , C.createdAt, CURRENT_TIMESTAMP) < 60\n" +
                "        then concat(TIMESTAMPDIFF(HOUR , C.createdAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "        when TIMESTAMPDIFF(DAY , C.createdAt, CURRENT_TIMESTAMP) < 30\n" +
                "         then concat(TIMESTAMPDIFF(DAY , C.createdAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "        when TIMESTAMPDIFF(MONTH , C.createdAt, CURRENT_TIMESTAMP) < 12\n" +
                "         then concat(TIMESTAMPDIFF(MONTH , C.createdAt, CURRENT_TIMESTAMP), '달 전')\n" +
                "        else concat(TIMESTAMPDIFF(YEAR , C.createdAt, CURRENT_TIMESTAMP), '년 전') end   AS 'Clip created',\n" +
                "    V.videoId   AS 'Video PK',\n" +
                "    userName    AS 'Video Reference',\n" +
                "    TIMEDIFF(C.start, C.end) AS 'Clip Length',\n" +
                "    (select count(*) from ClipViews CV where CV.clipId = C.clipId) AS 'Clip Views'\n" +
                "FROM\n" +
                "    ((Clips C inner join Videos V on C.videoId = V.videoId)\n" +
                "    inner join Users U on C.userId = U.userId)\n" +
                "WHERE\n" +
                "    U.userId = ?;";
        long    userClipsListQueryParams = userId;

        return  this.jdbcTemplate.query(
                userClipsListQuery,
                (rs, rowNum) -> new GetClipRes(
                        rs.getString("Clip title"),
                        rs.getString("clip url"),
                        rs.getString("Clip created"),
                        rs.getLong("Video PK"),
                        rs.getString("Video Reference"),
                        rs.getTime("Clip Length"),
                        rs.getInt("Clip Views")
                )
                ,
                userClipsListQueryParams
        );
    }

    public List<GetSubscriptChannelVideoRes>   getSubscriptChannelVideos(long  userId){
        String  getSubscriptChannelVideosQuery = "SELECT\n" +
                "    userName    AS 'Channel name',\n" +
                "    U.userId    AS 'Channel PK',\n" +
                "    V.videoId   AS 'Video PK',\n" +
                "    V.title     AS 'Video title',\n" +
                "    U.isCertified   AS 'certified user',\n" +
                "    (SELECT COUNT(*) FROM Views WHERE Views.videoId = V.videoId)    AS 'VIEWS'\n" +
                "FROM\n" +
                "    (Subscriptions S inner join Users U on S.subscriptUserId = U.userId)\n" +
                "    inner join Videos V on V.userId = U.userId\n" +
                "WHERE\n" +
                "    S.userId = ?\n" +
                "    AND U.status = 'ACTIVE'\n" +
                "ORDER BY\n" +
                "    V.createdAt DESC;";
        long    getSubscriptChannelVideosQueryParams = userId;

        return this.jdbcTemplate.query(getSubscriptChannelVideosQuery,
                (rs, rowNum) -> new GetSubscriptChannelVideoRes(
                        rs.getString("Channel name"),
                        rs.getLong("Channel PK"),
                        rs.getLong("Video PK"),
                        rs.getString("Video title"),
                        rs.getString("certified User"),
                        rs.getInt("VIEWS")
                )
                ,
                getSubscriptChannelVideosQueryParams);
    }

    public  int isExistingUser(long userId){
        String  checkExistingUserQuery = "SELECT\n" +
                "    EXISTS(SELECT * FROM Users  WHERE userId = ?);";
        long    checkExistingUserQueryParams = userId;

        return this.jdbcTemplate.queryForObject(checkExistingUserQuery, int.class,checkExistingUserQueryParams);
    }

    public int  checkExistingVideo(long videoId){
        String      checkExistingVideoQuery = "SELECT\n" +
                "    EXISTS(SELECT * FROM Videos  WHERE videoId = ?);";
        long        checkExistingVideoQueryParams = videoId;

        return this.jdbcTemplate.queryForObject(checkExistingVideoQuery, int.class, checkExistingVideoQueryParams);
    }
}