package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.community.model.PatchCommunityDescriptionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CommunityService {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final CommunityProvider     communityProvider;
    private final CommunityDao          communityDao;

    public CommunityService(CommunityProvider communityProvider, CommunityDao communityDao){
        this.communityDao = communityDao;
        this.communityProvider = communityProvider;
    }

    @Transactional
    public void modifyDescription(PatchCommunityDescriptionReq  patchCommunityDescriptionReq)   throws BaseException{
       // try{
            int result = communityDao.modifyDescription(patchCommunityDescriptionReq);
            if(result == 0){
                throw new BaseException(BaseResponseStatus.MODIFY_FAIL_USERNAME);
            }
      //  }catch (Exception e){
       //     throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
      //  }
    }
}