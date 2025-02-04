package com.cjh.blockbuster.service.user.service;

import com.cjh.blockbuster.api.model.vo.user.UserInfoSaveReq;
import com.cjh.blockbuster.api.model.vo.user.dto.BaseUserInfoDTO;
import com.cjh.blockbuster.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.cjh.blockbuster.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.cjh.blockbuster.service.user.repository.entity.UserDO;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author louzai
 * @date 2022-07-20
 */
public interface UserService {
    /**
     * 判断微信用户是否注册过
     *
     * @param wxuuid
     * @return
     */
    UserDO getWxUser(String wxuuid);

    List<SimpleUserInfoDTO> searchUser(String userName);

    /**
     * 保存用户详情
     *
     * @param req
     */
    void saveUserInfo(UserInfoSaveReq req);

    /**
     * 获取登录的用户信息,并更行丢对应的ip信息
     *
     * @param session  用户会话
     * @param clientIp 用户最新的登录ip
     * @return 返回用户基本信息
     */
    BaseUserInfoDTO getAndUpdateUserIpInfoBySessionId(String session, String clientIp);

    /**
     * 查询极简的用户信息
     *
     * @param userId
     * @return
     */
    SimpleUserInfoDTO querySimpleUserInfo(Long userId);

    /**
     * 查询用户基本信息
     * todo: 可以做缓存优化
     *
     * @param userId
     * @return
     */
    BaseUserInfoDTO queryBasicUserInfo(Long userId);


    /**
     * 批量查询用户基本信息
     *
     * @param userIds
     * @return
     */
    List<SimpleUserInfoDTO> batchQuerySimpleUserInfo(Collection<Long> userIds);

    /**
     * 批量查询用户基本信息
     *
     * @param userIds
     * @return
     */
    List<BaseUserInfoDTO> batchQueryBasicUserInfo(Collection<Long> userIds);

    /**
     * 查询用户主页信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    UserStatisticInfoDTO queryUserInfoWithStatistic(Long userId);

    /**
     * 查询用户基本信息，查询多个
     *
     * @param userIds
     * @return
     */
    List<BaseUserInfoDTO> queryBasicUserInfos(List<Long> userIds);

    Long getUserCount();
}
