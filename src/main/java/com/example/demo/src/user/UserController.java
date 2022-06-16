package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserProvider      userProvider;
    private final UserService       userService;
    private final JwtService        jwtService;

    @Autowired
    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetChannelInfoRes>> getChannelInfo(@PathVariable("userId")long  userId) {
        try{
            if(userProvider.checkExistingUser(userId) == 0){
                return  new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }
            List<GetChannelInfoRes> channelInfo = userProvider.getChannelInfo(userId);
                    return  new BaseResponse<List<GetChannelInfoRes>>(channelInfo);
            }
            catch (BaseException baseException){
                return new BaseResponse<>((baseException.getStatus()));
            }
    }

    @ResponseBody
    @PatchMapping("/{userId}/status")
    public BaseResponse<PatchUserStatusRes> patchUserStatus(@PathVariable("userId")long userId){
        try{
            if(userProvider.checkExistingUser(userId) == 0){
                return  new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }
            PatchUserStatusReq patchUserStatusReq = userProvider.getPatchUserStatusReq(userId);
            userService.patchUserStatus(patchUserStatusReq);
            PatchUserStatusRes patchUserStatusRes = userProvider.getPatchUserStatusRes(userId);
            return  new BaseResponse<>(patchUserStatusRes);
        }catch (BaseException baseException){
            return  new BaseResponse<>((baseException.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String>         patchUserName(@PathVariable("userId")long userId, @RequestBody  String userName){

        try{
            if(userProvider.checkExistingUser(userId) == 0){
                return  new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }

            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            PatchUserNameReq patchUserNameReq = new PatchUserNameReq(userId, userName);
            userService.patchUserName(patchUserNameReq);

            String result = "성공적으로 변경되었습니다.";
            return  new BaseResponse<String>(result);
        }
        catch (BaseException e){
            return new BaseResponse<>((e.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/subscription/{userId}")
    public BaseResponse<PostSubscriptionRes>    postSubscription(@PathVariable("userId")long userId, @RequestBody long channelId){
        try{
            if(userProvider.checkExistingUser(userId) == 0){
                return  new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }
            if(userProvider.checkExistingUser(channelId) == 0){
                return  new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }

            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            PostSubscriptionReq postSubscriptionReq = new PostSubscriptionReq(userId, channelId);
            PostSubscriptionRes postSubscriptionRes = userService.postChannelFollow(postSubscriptionReq);
            return  new BaseResponse<>(postSubscriptionRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes>    createUser(@RequestBody PostUserReq postUserReq){
        if(postUserReq.getUserName() == null) {
            return  new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_USERNAME);
        }
        if(postUserReq.getEmail() == null) {
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes>   logIn(@RequestBody PostLoginReq postLoginReq){
        if(postLoginReq.getEmail() == null){
            return new BaseResponse<>(BaseResponseStatus.POST_LOGIN_EMPTY_EMAIL);
        }
        if(postLoginReq.getPassword() == null){
            return new BaseResponse<>(BaseResponseStatus.POST_LOGIN_EMPTY_PASSWORD);
        }
        try{
            PostLoginRes postLoginRes = userProvider.getPwd(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }
}