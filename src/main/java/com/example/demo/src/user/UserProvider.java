package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserProvider {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao       userDao;
    private final JwtService    jwtService;

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService){

        this.userDao = userDao;
        this.jwtService = jwtService;
    }
    @Transactional(readOnly = true)
    public List<GetChannelInfoRes> getChannelInfo(long userId) throws BaseException {
        try{
            List<GetChannelInfoRes>   getChannelInfoRes = userDao.getChannelInfo(userId);
            return getChannelInfoRes;
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public PatchUserStatusReq     getPatchUserStatusReq(long  userId) throws BaseException{
        try{
            PatchUserStatusReq patchUserStatusReq = userDao.getStatusModifyReq(userId);
            return patchUserStatusReq;
        }catch (Exception e){
           throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public PatchUserStatusRes   getPatchUserStatusRes(long userId)  throws BaseException{
        try{
            PatchUserStatusRes patchUserStatusRes = userDao.getFinalUserStatus(userId);
            return patchUserStatusRes;
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public int              checkUserName(String userName)  throws BaseException{
        try{
            return userDao.checkUserName(userName);
        } catch (Exception exception){
            throw  new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public int              checkEmail(String email)    throws BaseException{
        try{
            return userDao.checkExistUser(email);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public PostLoginRes     getPwd(PostLoginReq postLoginReq)   throws BaseException{
        User    user = userDao.getPwd(postLoginReq);
        String  encryptedPwd;
        try{
            encryptedPwd = new SHA256().encrypt(postLoginReq.getPassword());
        }catch (Exception ignored){
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        if(user.getPassword().equals(encryptedPwd)){
            long    userId = user.getUserId();
            String  jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId, jwt);
        }
        else{
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }
    }
}