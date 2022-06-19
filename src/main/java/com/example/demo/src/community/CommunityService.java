package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.community.model.PatchCommunityDescriptionReq;
import com.example.demo.utils.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CommunityService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final CommunityProvider     communityProvider;
    private final CommunityDao          communityDao;
    private final JwtService            jwtService;

    @Transactional
    public void modifyDescription(PatchCommunityDescriptionReq  patchCommunityDescriptionReq)   throws BaseException{
            if(communityProvider.checkPost(patchCommunityDescriptionReq.getPostId()) == 0){
                throw   new BaseException(BaseResponseStatus.POST_NOT_EXISTS);
           }
            int result = communityDao.modifyDescription(patchCommunityDescriptionReq);
            if(result == 0){
                throw new BaseException(BaseResponseStatus.MODIFY_FAIL_USERNAME);
            }
            return;
    }
}