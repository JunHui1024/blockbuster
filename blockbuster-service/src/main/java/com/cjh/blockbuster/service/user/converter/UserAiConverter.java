package com.cjh.blockbuster.service.user.converter;

import com.cjh.blockbuster.api.model.enums.user.UserAIStatEnum;
import com.cjh.blockbuster.service.user.repository.entity.UserAiDO;
import com.cjh.blockbuster.service.user.service.help.UserRandomGenHelper;

/**
 * @author YiHui
 * @date 2023/6/27
 */
public class UserAiConverter {


    public static UserAiDO initAi(Long userId) {
        UserAiDO userAiDO = new UserAiDO();
        userAiDO.setUserId(userId);
        userAiDO.setStarNumber("");
        userAiDO.setStarType(0);
        userAiDO.setInviterUserId(0L);
        userAiDO.setStrategy(0);
        userAiDO.setInviteNum(0);
        userAiDO.setDeleted(0);
        userAiDO.setInviteCode(UserRandomGenHelper.genInviteCode(userId));
        userAiDO.setState(UserAIStatEnum.IGNORE.getCode());
        return userAiDO;
    }

}
