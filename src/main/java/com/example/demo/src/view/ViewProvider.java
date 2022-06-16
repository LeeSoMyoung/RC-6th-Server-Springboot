package com.example.demo.src.view;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ViewProvider {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final ViewDao   viewDao;
    private final UserDao   userDao;

    @Autowired
    public ViewProvider(ViewDao viewDao, UserDao userDao){
        this.userDao = userDao;
        this.viewDao = viewDao;
    }

    @Transactional(readOnly = true)
    public int  checkVideo(long videoId)    throws BaseException{
        try{
            return viewDao.checkExistingVideo(videoId);
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public int  checkUser(long  userId) throws BaseException{
        try{
            return  userDao.isExistingUser(userId);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}