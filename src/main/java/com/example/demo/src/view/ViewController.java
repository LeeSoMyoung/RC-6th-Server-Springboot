package com.example.demo.src.view;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.view.model.PostVideoViewReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/views")
public class ViewController {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ViewProvider    viewProvider;
    @Autowired
    private ViewService     viewService;

    public ViewController(ViewProvider viewProvider, ViewService viewService){
        this.viewProvider = viewProvider;
        this.viewService = viewService;
    }

    @ResponseBody
    @PostMapping("/{videoId}/{userId}")
    public BaseResponse<String> postVideoView(@PathVariable("videoId")long  videoId, @PathVariable("userId")long userId){
        try{
            if(viewProvider.checkUser(userId) == 0){
                return new BaseResponse<>(BaseResponseStatus.USER_NOT_EXISTS);
            }
            if(viewProvider.checkVideo(videoId) == 0){
                return new BaseResponse<>(BaseResponseStatus.VIDEO_NOT_EXISTS);
            }
            PostVideoViewReq postVideoViewReq = new PostVideoViewReq(videoId, userId);
            viewService.createVideoView(postVideoViewReq);
            String  result = "영상 뷰가 추가되었습니다.";
            return  new BaseResponse<String>(result);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }
}