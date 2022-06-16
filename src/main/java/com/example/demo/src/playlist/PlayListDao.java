package com.example.demo.src.playlist;

import com.example.demo.src.playlist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PlayListDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void    setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public List<GetPlayListsRes>    getPlayListsByUserId(long   userId){
        String  getPlayListByUserIdQuery = "SELECT\n" +
                "    playListId, userId, playListTitle\n" +
                "FROM PlayLists\n" +
                "WHERE userId = ?;";
        long  getPlayListByUserIdQueryParams = userId;

        return  this.jdbcTemplate.query(
                getPlayListByUserIdQuery,
                (rs, rowNum) -> new GetPlayListsRes(
                        rs.getLong("playListId"),
                        rs.getLong("userId"),
                        rs.getString("playListTitle")
                ),
                getPlayListByUserIdQueryParams
        );
    }

    public List<PlayListVideoRes>   playListVideos(long playListId){
        String  playListVideosQuery = "SELECT\n" +
                "        V.videoId           AS 'Video PK',\n" +
                "        playListTitle       AS `playlist title`,\n" +
                "        case when TIMESTAMPDIFF(SECOND, P.updatedAt,CURRENT_TIMESTAMP)<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, P.updatedAt,CURRENT_TIMESTAMP),'초 전')\n" +
                "            when TIMESTAMPDIFF(MINUTE , P.updatedAt,CURRENT_TIMESTAMP)<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE , P.updatedAt,CURRENT_TIMESTAMP),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR , P.updatedAt,CURRENT_TIMESTAMP)<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR , P.updatedAt,CURRENT_TIMESTAMP),'시간 전')\n" +
                "            when TIMESTAMPDIFF(DAY , P.updatedAt,CURRENT_TIMESTAMP)<30\n" +
                "            then concat(TIMESTAMPDIFF(DAY , P.updatedAt,CURRENT_TIMESTAMP),'일 전')\n" +
                "            when TIMESTAMPDIFF(MONTH ,P.updatedAt,CURRENT_TIMESTAMP) < 12\n" +
                "            then concat(TIMESTAMPDIFF(MONTH ,P.updatedAt,CURRENT_TIMESTAMP), '달 전')\n" +
                "            else concat(TIMESTAMPDIFF(YEAR,P.updatedAt,CURRENT_TIMESTAMP), '년 전')\n" +
                "        end AS `playlist updated At`,\n" +
                "        case when isPublic = 'N' then '비공개'\n" +
                "             else '공개' end as 'is public',\n" +
                "        U.userName          AS `playlist owner`,\n" +
                "        thumbnailUrl        AS `video thumbnail`,\n" +
                "        title               AS `video title`,\n" +
                "        Users.userName      AS `video uploader`,\n" +
                "        videoUrl            AS 'video',\n" +
                "        (select\n" +
                "        case when views < 1000\n" +
                "            then concat(views,'회')\n" +
                "            when views<10000\n" +
                "            then concat(round(views/1000,1),'천 회')\n" +
                "            when views < 100000000\n" +
                "            then concat(round(views/10000,1),'만 회')\n" +
                "            else concat(round(views/100000000,1),'억 회') end as 'video views'\n" +
                "         from (\n" +
                "             select count(*) as views\n" +
                "             from Views\n" +
                "             where Views.videoId = V.videoId\n" +
                "              ) a) as 'view counts',\n" +
                "    V.videoLength       AS 'Video length',\n" +
                "    V.playTime          AS 'user playtime'\n" +
                "FROM\n" +
                "    (((PlayLists P inner join PlayListVideos PV on PV.playListId = P.playListId)\n" +
                "    inner join Videos V\n" +
                "    on V.videoId = PV.videoId)\n" +
                "    inner join Users U\n" +
                "    on P.userId = U.userId and isDeleted = 'N' and status = 'ACTIVE')\n" +
                "    inner join Users on V.userId = Users.userId\n" +
                "WHERE P.playListId = ?\n" +
                "ORDER BY P.updatedAt DESC;";
        long    playListVideoQueryParmas = playListId;

        return this.jdbcTemplate.query(playListVideosQuery,
                (rs, rowNum) -> new PlayListVideoRes(
                        rs.getLong("Video PK"),
                        rs.getString("playlist title"),
                        rs.getString("playlist updated At"),
                        rs.getString("is public"),
                        rs.getString("playlist owner"),
                        rs.getString("video thumbnail"),
                        rs.getString("video title"),
                        rs.getString("video uploader"),
                        rs.getString("video"),
                        rs.getString("view counts"),
                        rs.getTime("Video length"),
                        rs.getTime("user playtime")
                )
                ,
                playListVideoQueryParmas);
    }

    public long     createdPlayList(PostPlayListReq postPlayListReq){
        String      createPlayListQuery = "INSERT INTO \n" +
                "PlayLists(userId, playListTitle)\n" +
                "VALUES (?, ?)";
        Object[]    createPlayListQueryParams = new Object[]{
                postPlayListReq.getUserId(), postPlayListReq.getPlayListTitle()
        };
        this.jdbcTemplate.update(createPlayListQuery, createPlayListQueryParams);

        String      getLastCreateIdQuery = "SELECT last_insert_id();";
        return this.jdbcTemplate.queryForObject(getLastCreateIdQuery, long.class);
    }

    public  int isExistingUser(long userId){
        String  checkExistingUserQuery = "SELECT\n" +
                "    EXISTS(SELECT * FROM Users  WHERE userId = ?);";
        long    checkExistingUserQueryParams = userId;

        return this.jdbcTemplate.queryForObject(checkExistingUserQuery, int.class,checkExistingUserQueryParams);
    }

    public  int isExistingPlayList(long playListId){
        String  checkExistingUserQuery = "SELECT\n" +
                "    EXISTS(SELECT * FROM Users  WHERE userId = ?);";
        long    checkExistingUserQueryParams = playListId;

        return this.jdbcTemplate.queryForObject(checkExistingUserQuery, int.class,checkExistingUserQueryParams);
    }
}