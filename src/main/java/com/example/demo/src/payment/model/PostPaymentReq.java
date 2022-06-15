package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPaymentReq {
    private long        userId;
    private String      card;
    private String      cardNum;
    private String      CVC;
    private String      password;
}