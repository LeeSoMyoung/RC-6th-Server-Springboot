package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CardInfo {
    private Long    paymentInfoId;
    private String  cardInfo;
    private String  cardNumber;
}