package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.InputBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao       userDao;
    private final UserProvider  userProvider;
    private final JwtService    jwtService;

    @Transactional
    public void   patchUserStatus(PatchUserStatusReq patchUserStatusReq)    throws BaseException{
        try{
            if(userProvider.checkExistingUser(patchUserStatusReq.getUserId()) == 0){
                throw   new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
            }

            int result = userDao.changeUserStatus(patchUserStatusReq);
            if(result == 0){
                throw new BaseException(BaseResponseStatus.FAIL_TO_MODIFY_COMMENT_ERROR);
            }
        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public void     patchUserName(PatchUserNameReq patchUserNameReq)    throws BaseException{
        try{
            if(userProvider.checkExistingUser(patchUserNameReq.getUserId()) == 0){
                throw   new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
            }

            int result = userDao.modifyUserName(patchUserNameReq);
            if(result == 0){
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        } catch (Exception exception){
            throw   new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public PostSubscriptionRes postChannelFollow(PostSubscriptionReq postSubscriptionReq)  throws BaseException{
       // try{

        if(userProvider.checkExistingUser(postSubscriptionReq.getUserId()) == 0){
            throw   new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }
        if(userProvider.checkExistingUser(postSubscriptionReq.getChannelId()) == 0){
            throw   new BaseException(BaseResponseStatus.USER_NOT_EXISTS);
        }

            long    subscriptionId = userDao.followChannel(postSubscriptionReq);
            PostSubscriptionRes postSubscriptionRes = new PostSubscriptionRes(subscriptionId, postSubscriptionReq.getUserId(), postSubscriptionReq.getChannelId());
            return postSubscriptionRes;
       // }catch (Exception exception){
        //    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
      //  }
    }
    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq)  throws BaseException{
        if(userProvider.checkUserName(postUserReq.getUserName())==1){
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_USERNAME);
        }
        if(userProvider.checkEmail(postUserReq.getEmail())==1){
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        }
        try{
            String pw = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pw);
        }catch (Exception ignored){
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            long    userId = userDao.createUser(postUserReq);
            String  jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt, userId);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public void createKakaoId(long userId, String email) throws BaseException{
        try{
            int result = userDao.createKakaoId(userId, email);
            if(result == 0){
                throw new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID);
            }
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}