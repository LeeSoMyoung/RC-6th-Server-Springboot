package com.example.demo.src.notification;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.notification.model.GetNotificationRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final NotificationProvider notificationProvider;
    @Autowired
    private final NotificationService   notificationService;
    @Autowired
    private final JwtService            jwtService;

    public NotificationController(NotificationProvider notificationProvider, NotificationService notificationService, JwtService jwtService){
        this.notificationProvider = notificationProvider;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetNotificationRes>> getNotifications(@PathVariable("userId")String id) {
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long    userId = Long.parseLong(id);
            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<GetNotificationRes> notificationList = notificationProvider.getNotifications(userId);
            return  new BaseResponse<List<GetNotificationRes>>(notificationList);
        }
        catch (BaseException baseException){
            return  new BaseResponse<>(baseException.getStatus());
        }
    }
}