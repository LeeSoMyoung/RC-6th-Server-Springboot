package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.PostLoginRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    @Autowired
    private final   OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService){this.oAuthService = oAuthService;}

    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<PostLoginRes>   kakaoLogIn(@RequestParam("code")String  code){
        try{
            return new BaseResponse<PostLoginRes>(oAuthService.kakaoLogIn(code));
        }
        catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/kakao/logout")
    public BaseResponse<String>         kakaoLogOut(@RequestParam("code") String code){
        try{
            String  res = "로그아웃에 성공하였습니다.";
            oAuthService.logOut(code);
            return new BaseResponse<String>(res);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }
}