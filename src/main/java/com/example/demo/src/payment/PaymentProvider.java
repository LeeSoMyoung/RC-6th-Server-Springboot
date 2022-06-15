package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.payment.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PaymentDao paymentDao;

    public PaymentProvider(PaymentDao paymentDao){
        this.paymentDao = paymentDao;
    }

    @Transactional(readOnly = true)
    public List<GetPaymentRes> getPaymentInfos(long userId) throws BaseException {
        try{
            List<GetPaymentRes> getPaymentRes = paymentDao.getPaymentInfo(userId);
            return  getPaymentRes;
        }
        catch(Exception exception){
            throw  new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<CardInfo>   getCardInfos(long   userId) throws BaseException{
        try{
            List<CardInfo> cardInfoList = paymentDao.getCardInfoLists(userId);
            return  cardInfoList;
        }
        catch(Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public int checkCardInfo(String card, String cardNum)   throws BaseException{
        try{
            return paymentDao.checkCardInfos(card, cardNum);
        }
        catch(Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}