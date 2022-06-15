package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPaymentRes {
    private long    paymentId;
    private long    userId;
}