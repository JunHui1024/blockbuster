package com.cjh.blockbuster.service.user.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.blockbuster.service.user.repository.entity.UserAiHistoryDO;
import com.cjh.blockbuster.service.user.repository.mapper.UserAiHistoryMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserAiHistoryDao extends ServiceImpl<UserAiHistoryMapper, UserAiHistoryDO> {

    @Resource
    private UserAiHistoryMapper userAiHistoryMapper;
}

