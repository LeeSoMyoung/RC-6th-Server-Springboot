package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Payment {
    @Id
    private long    paymentId;

    private String      card;
    private String      cardNumber;
    private Timestamp   createdAt;
    private Timestamp   updatedAt;
    private String      CVC;
    private String      password;
    private String      status;
}