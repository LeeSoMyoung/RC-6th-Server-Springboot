package com.example.demo.src.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPaymentRes {
    private int         price; // payment price;
    private String      receiptDescription;
    private String      purchasedStatus;
    private int         membershipPrice;
    private String      membershipDescription;
    private long        receiptId;
}