package com.example.demo.src.oauth.config;

import com.example.demo.config.secret.Secret;
import com.example.demo.src.oauth.model.KakaoUser;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth {

    public KakaoUser    getKakaoUser(String code){
        String      accessToken = getKakaoAccessToken(code);
        KakaoUser   kakaoUser = getKakaoUserByToken(accessToken);

        return kakaoUser;
    }

    private String       getKakaoAccessToken(String  code){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String>   params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", Secret.REST_API_KEY);
        params.add("redirect_uri", Secret.REDIRECT_URI);
        params.add("code", code);

        RestTemplate    restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenReq = new HttpEntity<>(params, headers);

        ResponseEntity<String>  response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenReq,
                String.class
        );

        String tokenJson = response.getBody();
        JSONObject  jsonObject = new JSONObject(tokenJson);
        String      accessToken = jsonObject.getString("access_token");

        return accessToken;
    }

    private KakaoUser   getKakaoUserByToken(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate    restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String,String>>    kakaoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoRequest,
                String.class
        );

        JSONObject  body = new JSONObject(response.getBody());
        long        userId = body.getLong("id");
        String      email = body.getJSONObject("kakao_account").getString("email");
        String      userName = body.getJSONObject("properties").getString("nickname");

        return new KakaoUser(userId, email, userName);
    }

    public void     kakaoLogOut(String code){
        HttpHeaders headers = new HttpHeaders();
        String      accessToken = getKakaoAccessToken(code);
        headers.add("Authorization", "Bearer "+accessToken);

        RestTemplate    restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String,String>>    logoutRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                logoutRequest,
                String.class
        );
    }
}