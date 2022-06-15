package com.example.demo.src.user;

import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public int   changeUserStatus(PatchUserStatusReq patchUserStatusReq){
        String      userStatus = patchUserStatusReq.getStatus().equals("ACTIVE") ? "DEACTIVE":"ACTIVE";
        String      changeUserStatusQuery = "UPDATE Users\n" +
                "SET status=?\n" +
                "WHERE userId = ?;";
        Object[]    changeUserStatusQueryParams = new Object[]{userStatus, patchUserStatusReq.getUserId()};

        return  this.jdbcTemplate.update(changeUserStatusQuery, changeUserStatusQueryParams);
    }

    public int  modifyUserName(PatchUserNameReq patchUserNameReq){
        String      modifyUserNameQuery = "UPDATE Users\n" +
                "SET userName=?\n" +
                "WHERE userId = ?;";
        Object[]    modifyUserNameQueryParmas = new Object[]{patchUserNameReq.getUserName(), patchUserNameReq.getUserId()};

        return  this.jdbcTemplate.update(modifyUserNameQuery, modifyUserNameQueryParmas);
    }

    public List<GetChannelInfoRes>    getChannelInfo(long userId){
        String  getChannelInfoQuery = "SELECT\n" +
                "    playListId      AS 'playlist PK',\n" +
                "    profilePicUrl   AS 'profile picture',\n" +
                "    userName        AS 'Channel name',\n" +
                "    (select count(*) from Subscriptions S where S.subscriptionId = U.userId)    AS 'the number of the followers',\n" +
                "    playListTitle   AS 'playList name'\n" +
                "FROM\n" +
                "    (Users U right join PlayLists PL on U.userId = PL.userId)\n" +
                "WHERE\n" +
                "    U.userId = ? and U.status = 'ACTIVE';";
        long  getChannelInfoQueryParams = userId;

        return this.jdbcTemplate.query(getChannelInfoQuery,
                (rs, rowNum) -> new GetChannelInfoRes(
                        rs.getLong("playlist PK"),
                        rs.getString("profile picture"),
                        rs.getString("Channel name"),
                        rs.getInt("the number of the followers"),
                        rs.getString("playList name")
                )
                ,getChannelInfoQueryParams);
    }

    public PatchUserStatusReq   getStatusModifyReq(long userId){
        String  getUserStatusQuery = "SELECT \n" +
                "status FROM Users WHERE userId = ?;";
        long    getUserStatusQueryParams = userId;

        return  this.jdbcTemplate.queryForObject(getUserStatusQuery,
                (rs, rowNum) -> new PatchUserStatusReq(
                        userId,
                        rs.getString("status")
                )
                ,getUserStatusQueryParams);
    }

    public PatchUserStatusRes   getFinalUserStatus(long userId){
        String  getUserStatusQuery = "SELECT \n" +
                "status FROM Users \n" +
                "WHERE userId = ?";
        long    getUserStatusQueryParams = userId;

        return this.jdbcTemplate.queryForObject(getUserStatusQuery,
                (rs, rowNum) -> new PatchUserStatusRes(
                        userId,
                        rs.getString("status")
                )
                ,getUserStatusQueryParams);
    }

    public long   followChannel(PostSubscriptionReq postSubscriptionReq){
        String      followChannelQuery = "INSERT INTO Subscriptions(userId, subscriptUserId) VALUES(?,?);";
        Object[]    followChannelQueryParams = new Object[]{postSubscriptionReq.getUserId(), postSubscriptionReq.getChannelId()};
        this.jdbcTemplate.update(followChannelQuery, followChannelQueryParams);

        String      lastInsertIdQuery = "SELECT last_insert_id();";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, long.class);
    }

    public int  checkExistUser(String email){
        String  checkEmailQuery = "SELECT EXISTS(SELECT\n" +
                "                  email\n" +
                "              FROM Users\n" +
                "              WHERE email = ?);";
        String  checkEmailQueryParams = email;

        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailQueryParams);
    }

    public int  checkUserName(String userName){
        String      checkUserNameQuery = "SELECT EXISTS(SELECT\n" +
                "                  userName\n" +
                "              FROM Users\n" +
                "              WHERE userName = ?);";
        String      checkUserNameQueryParams = userName;

        return  this.jdbcTemplate.queryForObject(checkUserNameQuery, int.class, checkUserNameQueryParams);
    }

    public long createUser(PostUserReq postUserReq){
        String      createUserQuery = "INSERT INTO Users(userName, email, password, countryId, profilePicUrl)\n" +
                "VALUES (?, ?, ?, ?, ?);";
        Object[]    createUserQueryParams = new Object[] {
                postUserReq.getUserName(), postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getCountryId(), postUserReq.getProfilePicUrl()
        };
        this.jdbcTemplate.update(createUserQuery, createUserQueryParams);

        String      getUserIdQuery = "SELECT last_insert_id();";
        return      this.jdbcTemplate.queryForObject(getUserIdQuery, long.class);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String      getPwdQuery = "SELECT\n" +
                "    userId, email, password, userName\n" +
                "FROM\n" +
                "    Users\n" +
                "WHERE email = ?;";
        String      getPwdQueryParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("userId"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("password")
                ),getPwdQueryParams);
    }
}