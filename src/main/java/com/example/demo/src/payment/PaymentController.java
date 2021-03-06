package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.payment.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PaymentService paymentService;
    @Autowired
    private final PaymentProvider paymentProvider;
    @Autowired
    private final JwtService      jwtService;

    public PaymentController(PaymentService paymentService, PaymentProvider paymentProvider, JwtService jwtService){
        this.paymentProvider = paymentProvider;
        this.paymentService = paymentService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/card-infos")
    public BaseResponse<List<CardInfo>> getCardInfos(@RequestParam("id")String id){
        try {
            if(!ValidationRegex.isDigit(id)){
                return  new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long    userId = Long.parseLong(id);
            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<CardInfo> getCardResList = paymentProvider.getCardInfos(userId);
            return new BaseResponse<>(getCardResList);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/receipts")
    public BaseResponse<List<GetPaymentRes>>    getPaymentInfos(@RequestParam("id")String id){
        try {
            if(!ValidationRegex.isDigit(id)){
                return  new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long    userId = Long.parseLong(id);
            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<GetPaymentRes> getPaymentRes = paymentProvider.getPaymentInfos(userId);
            return new BaseResponse<>(getPaymentRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/{userId}")
    public BaseResponse<PostPaymentRes> createPaymentInfo(@PathVariable("userId")String id,@RequestBody PostPaymentReq postPaymentReq){
        try{
            if(postPaymentReq.getCard()==null){
                return  new BaseResponse<>(BaseResponseStatus.POST_PAYMENT_EMPTY);
            }
            if(postPaymentReq.getCardNum()==null){
                return  new BaseResponse<>(BaseResponseStatus.POST_PAYMENT_EMPTY);
            }
            if(postPaymentReq.getCVC()==null){
                return  new BaseResponse<>(BaseResponseStatus.POST_PAYMENT_EMPTY);
            }
            if(postPaymentReq.getPassword()==null){
                return  new BaseResponse<>(BaseResponseStatus.POST_PAYMENT_EMPTY);
            }
            if(postPaymentReq.getCVC()==null){
                return  new BaseResponse<>(BaseResponseStatus.POST_PAYMENT_EMPTY);
            }

            if(!ValidationRegex.isDigit(id)){
                return  new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long    userId = Long.parseLong(id);
            long    jwtUserId = jwtService.getUserId();

            if(jwtUserId != userId){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }
            PostPaymentRes postPaymentRes = paymentService.createPaymentInfo(postPaymentReq);
            return  new BaseResponse<PostPaymentRes>(postPaymentRes);
        }
        catch (BaseException baseException){
            return  new BaseResponse<>((baseException.getStatus()));
        }
    }
}