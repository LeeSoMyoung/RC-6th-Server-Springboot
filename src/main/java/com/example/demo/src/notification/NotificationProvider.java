package com.example.demo.src.notification;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.notification.model.GetNotificationRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationProvider {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final   NotificationDao notificationDao;

    @Autowired
    public NotificationProvider(NotificationDao notificationDao){
        this.notificationDao = notificationDao;
    }

    @Transactional(readOnly = true)
    public List<GetNotificationRes> getNotifications(long userId)   throws BaseException{
        try{
            List<GetNotificationRes> notificationList = notificationDao.getNotifications(userId);
            return notificationList;
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}