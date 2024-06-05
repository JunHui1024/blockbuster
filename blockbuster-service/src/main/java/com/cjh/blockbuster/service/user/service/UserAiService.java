package com.cjh.blockbuster.service.user.service;

import com.cjh.blockbuster.api.model.enums.ai.AISourceEnum;
import com.cjh.blockbuster.api.model.vo.chat.ChatItemVo;

public interface UserAiService {
    /**
     * 保存聊天历史记录
     *
     * @param source
     * @param user
     * @param item
     */
    void pushChatItem(AISourceEnum source, Long user, ChatItemVo item);

    /**
     * 获取用户的最大聊天次数
     *
     * @param userId
     * @return
     */
    int getMaxChatCnt(Long userId);
}
