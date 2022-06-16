package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.payment.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PaymentService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentDao paymentDao;
    private final PaymentProvider paymentProvider;

    @Autowired
    public PaymentService(PaymentDao paymentDao, PaymentProvider paymentProvider){
        this.paymentDao = paymentDao;
        this.paymentProvider = paymentProvider;
    }

    @Transactional
    public PostPaymentRes createPaymentInfo(PostPaymentReq postPaymentReq)   throws BaseException{
        try{
            long    paymentId = paymentDao.createPaymentInfo(postPaymentReq);
            return new PostPaymentRes(paymentId, postPaymentReq.getUserId());
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}