package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.community.model.GetCommunityPostRes;
import com.example.demo.src.community.model.PatchCommunityDescriptionReq;
import com.example.demo.utils.ValidationRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommunityService      communityService;
    @Autowired
    private final CommunityProvider communityProvider;

    public CommunityController(CommunityService communityService, CommunityProvider communityProvider){
        this.communityService = communityService;
        this.communityProvider = communityProvider;
    }

    @ResponseBody
    @GetMapping("/users/{userId}")
    public BaseResponse<List<GetCommunityPostRes>>    getCommunityPost(@PathVariable("userId")String id){
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        userId = Long.parseLong(id);
            List<GetCommunityPostRes>   postList = communityProvider.getPostList(userId);
            return new BaseResponse<List<GetCommunityPostRes>>(postList);
        }catch (BaseException baseException){
            return  new BaseResponse<>(baseException.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/posts/{postId}")
    public BaseResponse<String>     patchPostDescription(@PathVariable("postId")String id, @RequestBody String description){
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        postId = Long.parseLong(id);

            PatchCommunityDescriptionReq patchCommunityDescriptionReq = new PatchCommunityDescriptionReq(postId, description);
            communityService.modifyDescription(patchCommunityDescriptionReq);

            String result = "포스팅 내용이 성공적으로 수정되었습니다.";
            return new BaseResponse<String>(result);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/posts/{postId}")
    public BaseResponse<List<GetCommunityPostRes>>    getPostByPostId(@PathVariable("postId")String id){
        try{
            if(!ValidationRegex.isDigit(id)){
                return new BaseResponse<>(BaseResponseStatus.INVALID_ID);
            }

            long        postId = Long.parseLong(id);

            List<GetCommunityPostRes> getCommunityPostRes = communityProvider.getPost(postId);
            return  new BaseResponse<List<GetCommunityPostRes>>(getCommunityPostRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }
}