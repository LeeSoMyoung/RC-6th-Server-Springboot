package com.example.demo.src.community;

import com.example.demo.src.community.model.GetCommunityPostRes;
import com.example.demo.src.community.model.PatchCommunityDescriptionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommunityDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public List<GetCommunityPostRes> getPostList(long   channelId){
        String  getPostListQuery = "SELECT\n" +
                "       Users.userId                                                                         AS 'channel PK',\n" +
                "       profilePicUrl                                                                        AS 'channel profile picture',\n" +
                "       isCertified                                                                          AS 'check certified user',\n" +
                "       userName                                                                             AS 'channel name',\n" +
                "       Posts.description                                                                    AS 'posting description',\n" +
                "       case\n" +
                "            when TIMESTAMPDIFF(SECOND ,Posts.createdAt,CURRENT_TIMESTAMP) < 60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND ,Posts.createdAt,CURRENT_TIMESTAMP), '초 전')\n" +
                "            when TIMESTAMPDIFF(MINUTE ,Posts.createdAt,CURRENT_TIMESTAMP) < 60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE ,Posts.createdAt,CURRENT_TIMESTAMP), '분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR,Posts.createdAt,CURRENT_TIMESTAMP) < 24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR,Posts.createdAt,CURRENT_TIMESTAMP), '시간 전')\n" +
                "            when TIMESTAMPDIFF(HOUR,Posts.createdAt,CURRENT_TIMESTAMP) < 48\n" +
                "            then '하루 전'\n" +
                "            when TIMESTAMPDIFF(DAY,Posts.createdAt,CURRENT_TIMESTAMP) < 30\n" +
                "            then concat(TIMESTAMPDIFF(YEAR ,Posts.createdAt,CURRENT_TIMESTAMP), '일 전')\n" +
                "            when TIMESTAMPDIFF(MONTH ,Posts.createdAt,CURRENT_TIMESTAMP) < 12\n" +
                "            then concat(TIMESTAMPDIFF(MONTH ,Posts.createdAt,CURRENT_TIMESTAMP), '달 전')\n" +
                "            else concat(TIMESTAMPDIFF(YEAR,Posts.createdAt,CURRENT_TIMESTAMP), '년 전')\n" +
                "        end                                                                                 AS 'createdAt',\n" +
                "        case when TIMESTAMPDIFF(SECOND ,Posts.updatedAt,Posts.createdAt) = 0\n" +
                "            then '수정 되지 않음'\n" +
                "            else '수정됨' end                                                                AS 'check modification',\n" +
                "       contentUrl AS 'posting picture'\n" +
                "FROM\n" +
                "    (SELECT userId, description, createdAt, updatedAt, contentUrl, postId\n" +
                "     FROM ChannelCommunity C right join\n" +
                "        (\n" +
                "            SELECT description, createdAt, updatedAt, contentUrl, communityId, YCP.postId\n" +
                "            FROM (\n" +
                "                YoutubeCommunityPosts YCP left join YoutubeCommunityPostContents YCPC\n" +
                "                on YCP.postId = YCPC.postId\n" +
                "                 )\n" +
                "        ) PostContents on C.communityId = PostContents.communityId) Posts\n" +
                "        inner join Users on Users.userId = Posts.userId and Users.status = 'ACTIVE'\n" +
                "WHERE Users.userId = ?\n" +
                "ORDER BY postId;";
        long    getPostListQueryParams = channelId;

        return this.jdbcTemplate.query(getPostListQuery,
                (rs, rowNum) -> new GetCommunityPostRes(
                        rs.getLong("channel PK"),
                        rs.getString("channel profile picture"),
                        rs.getString("check certified user"),
                        rs.getString("channel name"),
                        rs.getString("posting description"),
                        rs.getString("createdAt"),
                        rs.getString("check modification"),
                        rs.getString("posting picture")
                )
                ,getPostListQueryParams);
    }

    public List<GetCommunityPostRes> getCommunityPost(long    postId){
        String  getPostQuery = "SELECT\n" +
                "       Users.userId                                                                         AS 'channel PK',\n" +
                "       profilePicUrl                                                                        AS 'channel profile picture',\n" +
                "       isCertified                                                                          AS 'check certified user',\n" +
                "       userName                                                                             AS 'channel name',\n" +
                "       Posts.description                                                                    AS 'posting description',\n" +
                "       case\n" +
                "            when TIMESTAMPDIFF(SECOND ,Posts.createdAt,CURRENT_TIMESTAMP) < 60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND ,Posts.createdAt,CURRENT_TIMESTAMP), '초 전')\n" +
                "            when TIMESTAMPDIFF(MINUTE ,Posts.createdAt,CURRENT_TIMESTAMP) < 60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE ,Posts.createdAt,CURRENT_TIMESTAMP), '분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR,Posts.createdAt,CURRENT_TIMESTAMP) < 24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR,Posts.createdAt,CURRENT_TIMESTAMP), '시간 전')\n" +
                "            when TIMESTAMPDIFF(HOUR,Posts.createdAt,CURRENT_TIMESTAMP) < 48\n" +
                "            then '하루 전'\n" +
                "            when TIMESTAMPDIFF(DAY,Posts.createdAt,CURRENT_TIMESTAMP) < 30\n" +
                "            then concat(TIMESTAMPDIFF(YEAR ,Posts.createdAt,CURRENT_TIMESTAMP), '일 전')\n" +
                "            when TIMESTAMPDIFF(MONTH ,Posts.createdAt,CURRENT_TIMESTAMP) < 12\n" +
                "            then concat(TIMESTAMPDIFF(MONTH ,Posts.createdAt,CURRENT_TIMESTAMP), '달 전')\n" +
                "            else concat(TIMESTAMPDIFF(YEAR,Posts.createdAt,CURRENT_TIMESTAMP), '년 전')\n" +
                "        end                                                                                 AS 'createdAt',\n" +
                "        case when TIMESTAMPDIFF(SECOND ,Posts.updatedAt,Posts.createdAt) = 0\n" +
                "            then '수정 되지 않음'\n" +
                "            else '수정됨' end                                                                AS 'check modification',\n" +
                "       contentUrl AS 'posting picture'\n" +
                "FROM\n" +
                "    (SELECT userId, description, createdAt, updatedAt, contentUrl, postId\n" +
                "     FROM ChannelCommunity C right join\n" +
                "        (\n" +
                "            SELECT description, createdAt, updatedAt, contentUrl, communityId, YCP.postId\n" +
                "            FROM (\n" +
                "                YoutubeCommunityPosts YCP left join YoutubeCommunityPostContents YCPC\n" +
                "                on YCP.postId = YCPC.postId\n" +
                "                 )\n" +
                "        ) PostContents on C.communityId = PostContents.communityId) Posts\n" +
                "        inner join Users on Users.userId = Posts.userId and Users.status = 'ACTIVE'\n" +
                "WHERE Posts.postId = ?;";
        long    getPostQueryParams = postId;

        return this.jdbcTemplate.query(getPostQuery,
                (rs, rowNum) -> new GetCommunityPostRes(
                        rs.getLong("channel PK"),
                        rs.getString("channel profile picture"),
                        rs.getString("check certified user"),
                        rs.getString("channel name"),
                        rs.getString("posting description"),
                        rs.getString("createdAt"),
                        rs.getString("check modification"),
                        rs.getString("posting picture")
                )
                ,getPostQueryParams);
    }

    public int  modifyDescription(PatchCommunityDescriptionReq patchCommunityDescriptionReq){
        String      modifyDescriptionQuery = "UPDATE YoutubeCommunityPosts \n" +
                "SET description = ? WHERE postId = ?;";
        Object[]    modifyDescriptionQueryParams = new Object[]{patchCommunityDescriptionReq.getDescription(), patchCommunityDescriptionReq.getPostId()};

        return this.jdbcTemplate.update(modifyDescriptionQuery, modifyDescriptionQueryParams);
    }

    public  int isExistingUser(long userId){
        String  checkExistingUserQuery = "SELECT\n" +
                "    EXISTS(SELECT * FROM Users  WHERE userId = ?);";
        long    checkExistingUserQueryParams = userId;

        return this.jdbcTemplate.queryForObject(checkExistingUserQuery, int.class,checkExistingUserQueryParams);
    }

    public  int checkPosts(long postId){
        String      checkPostQuery = "SELECT\n" +
                "    EXISTS(SELECT * FROM YoutubeCommunityPosts  WHERE postId = ?);";
        long        checkPostQueryParams = postId;

        return this.jdbcTemplate.queryForObject(checkPostQuery, int.class, checkPostQueryParams);
    }
}