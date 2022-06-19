package com.example.demo.src.view;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.view.model.PostVideoViewReq;
import com.example.demo.utils.ValidationRegex;
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
    public BaseResponse<String> postVideoView(@PathVariable("videoId")String  video, @PathVariable("userId")String user){
        try{
            if(!ValidationRegex.isDigit(video) || !ValidationRegex.isDigit(user)){
                return  new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long    videoId = Long.parseLong(video);
            long    userId = Long.parseLong(user);

            PostVideoViewReq postVideoViewReq = new PostVideoViewReq(videoId, userId);
            viewService.createVideoView(postVideoViewReq);
            String  result = "영상 뷰가 추가되었습니다.";
            return  new BaseResponse<String>(result);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }
}