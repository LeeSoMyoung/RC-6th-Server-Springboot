package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserProvider      userProvider;
    private final UserService       userService;
    private final JwtService        jwtService;

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetChannelInfoRes>> getChannelInfo(@PathVariable("userId")long  userId) {
        try{
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

            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
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
        try{
            if(postUserReq.getUserName() == null) {
                return  new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_USERNAME);
            }
            if(postUserReq.getEmail() == null) {
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
            }

            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes>   logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            //String  email = postLoginReq.getEmail();
            PostLoginRes postLoginRes = userProvider.getPwd(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }
}