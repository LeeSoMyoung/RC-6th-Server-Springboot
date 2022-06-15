package com.example.demo.src.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthProvider {
    private final   OAuthDao oAuthDao;

    @Autowired
    public  OAuthProvider(OAuthDao oAuthDao){
        this.oAuthDao = oAuthDao;
    }
}