package com.example.demo.src.oauth;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthService  {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final   OAuthProvider   oAuthProvider;
    private final   OAuthDao        oAuthDao;
    private final   JwtService      jwtService;

    @Autowired
    public OAuthService(OAuthProvider oAuthProvider, OAuthDao oAuthDao, JwtService jwtService){
        this.oAuthDao = oAuthDao;
        this.oAuthProvider = oAuthProvider;
        this.jwtService = jwtService;
    }
}