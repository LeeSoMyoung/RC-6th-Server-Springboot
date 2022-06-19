package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.oauth.config.KakaoOAuth;
import com.example.demo.src.oauth.model.KakaoUser;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostLoginRes;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OAuthService {

    private final   KakaoOAuth        kakaoOAuth;
    private final   UserProvider      userProvider;
    private final   UserService       userService;
    private final   JwtService        jwtService;

    public OAuthService(KakaoOAuth kakaoOAuth, UserProvider userProvider, UserService userService, JwtService jwtService){
        this.jwtService = jwtService;
        this.userProvider = userProvider;
        this.userService = userService;
        this.kakaoOAuth = kakaoOAuth;
    }

    public PostLoginRes     kakaoLogIn(String   code)   throws BaseException{
        KakaoUser   kakaoUser = kakaoOAuth.getKakaoUser(code);
        long        userId = kakaoUser.getUserId();
        String      email = kakaoUser.getEmail();
        String      userName = kakaoUser.getUserName();
        List<GetUserRes> user = userProvider.getUserByKakaoId(userId);
        if(user.size() == 0){
            List<GetUserRes> emailAccount = userProvider.getUserByEmail(email);
            if(emailAccount.size()>0){
                userService.createKakaoId(userId, email);
            }
            else{
                PostUserReq postUserReq = new PostUserReq(
                        userName, email, "", 82, ""
                );
                userService.createUser(postUserReq);
                userService.createKakaoId(userId, email);
            }
        }
        try{
            long    kakaoId = userProvider.getUserByKakaoId(userId).get(0).getUserId();
            String  jwt = jwtService.createJwt(userId);
            return  new PostLoginRes(kakaoId, jwt);
         }
          catch (Exception ignored){
              throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
          }
    }

    public void     logOut(String code) throws BaseException{
      //  try{
            kakaoOAuth.kakaoLogOut(code);
       // }
       // catch (Exception exception){
      //      throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
     //   }
    }
}