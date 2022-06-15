package com.example.demo.src.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final   NotificationProvider notificationProvider;
    private final   NotificationDao      notificationDao;

    @Autowired
    public NotificationService(NotificationDao notificationDao, NotificationProvider notificationProvider){
        this.notificationDao = notificationDao;
        this.notificationProvider = notificationProvider;
    }


}