package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    @Autowired
    private final OAuthService      oAuthService;
    @Autowired
    private final OAuthProvider     oAuthProvider;

    public OAuthController(OAuthService oAuthService, OAuthProvider oAuthProvider){
        this.oAuthProvider = oAuthProvider;
        this.oAuthService = oAuthService;
    }

    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<String> kakaoCallback(@RequestParam String code){
        String response = "성공적으로 카카오 로그인 API 코드를 불러왔습니다.";
        return  new BaseResponse<String>(response);
    }
}