package com.example.demo.src.notification;

import com.example.demo.src.notification.model.GetNotificationRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NotificationDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public List<GetNotificationRes> getNotifications(long   userId){
        String  getNotificationsQuery = "SELECT\n" +
                "    V.videoId     AS 'Video PK',\n" +
                "    U.userId    AS 'Channel PK',\n" +
                "    U.userName  AS 'Channel Name',\n" +
                "    U.profilePicUrl AS 'Channel Profile Pic',\n" +
                "    V.thumbnailUrl  AS 'Video Thumbnail',\n" +
                "    concat(N.description, V.title)  AS 'Notification description',\n" +
                "    case when TIMESTAMPDIFF(SECOND , N.createdAt, CURRENT_TIMESTAMP) < 60\n" +
                "         then concat(TIMESTAMPDIFF(SECOND , N.createdAt, CURRENT_TIMESTAMP), '초 전')\n" +
                "        when TIMESTAMPDIFF(MINUTE , N.createdAt, CURRENT_TIMESTAMP) < 60\n" +
                "        then concat(TIMESTAMPDIFF(MINUTE , N.createdAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "        when TIMESTAMPDIFF(HOUR , N.createdAt, CURRENT_TIMESTAMP) < 60\n" +
                "        then concat(TIMESTAMPDIFF(HOUR , N.createdAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "        when TIMESTAMPDIFF(DAY , N.createdAt, CURRENT_TIMESTAMP) < 30\n" +
                "         then concat(TIMESTAMPDIFF(DAY , N.createdAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "        when TIMESTAMPDIFF(MONTH , N.createdAt, CURRENT_TIMESTAMP) < 12\n" +
                "         then concat(TIMESTAMPDIFF(MONTH , N.createdAt, CURRENT_TIMESTAMP), '달 전')\n" +
                "        else concat(TIMESTAMPDIFF(YEAR , N.createdAt, CURRENT_TIMESTAMP), '년 전') end   AS 'Notification created'\n" +
                "FROM\n" +
                "    (Notifications N inner join Users U on N.alarmingChannel = U.userId)\n" +
                "    inner join Videos V on V.videoId = N.videoId\n" +
                "WHERE\n" +
                "    N.userId = ?\n" +
                "    and U.status = 'ACTIVE';";
        long       getNotificationQueryParams = userId;

        return this.jdbcTemplate.query(getNotificationsQuery,
                (rs, rowNum) -> new GetNotificationRes(
                        rs.getLong("Video PK"),
                        rs.getLong("Channel PK"),
                        rs.getString("Channel Name"),
                        rs.getString("Channel Profile Pic"),
                        rs.getString("Video Thumbnail"),
                        rs.getString("Notification description"),
                        rs.getString("Notification created")
                )
                ,getNotificationQueryParams);

    }
}