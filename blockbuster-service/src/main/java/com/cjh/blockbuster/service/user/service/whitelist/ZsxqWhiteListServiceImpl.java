package com.cjh.blockbuster.service.user.service.whitelist;

import com.cjh.blockbuster.api.model.enums.user.UserAIStatEnum;
import com.cjh.blockbuster.api.model.exception.ExceptionUtil;
import com.cjh.blockbuster.api.model.vo.PageVo;
import com.cjh.blockbuster.api.model.vo.constants.StatusEnum;
import com.cjh.blockbuster.api.model.vo.user.SearchZsxqUserReq;
import com.cjh.blockbuster.api.model.vo.user.ZsxqUserPostReq;
import com.cjh.blockbuster.api.model.vo.user.dto.ZsxqUserInfoDTO;
import com.cjh.blockbuster.service.user.converter.UserStructMapper;
import com.cjh.blockbuster.service.user.repository.dao.UserAiDao;
import com.cjh.blockbuster.service.user.repository.dao.UserDao;
import com.cjh.blockbuster.service.user.repository.entity.UserAiDO;
import com.cjh.blockbuster.service.user.repository.entity.UserDO;
import com.cjh.blockbuster.service.user.repository.entity.UserInfoDO;
import com.cjh.blockbuster.service.user.repository.params.SearchZsxqWhiteParams;
import com.cjh.blockbuster.service.user.service.ZsxqWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author 沉默王二
 * @date 6/29/23
 */
@Service
public class ZsxqWhiteListServiceImpl implements ZsxqWhiteListService {
    @Autowired
    private UserAiDao userAiDao;
    @Autowired
    private UserDao userDao;

    @Override
    public PageVo<ZsxqUserInfoDTO> getList(SearchZsxqUserReq req) {
        SearchZsxqWhiteParams  params = UserStructMapper.INSTANCE.toSearchParams(req);
        // 查询知识星球用户
        List<ZsxqUserInfoDTO> zsxqUserInfoDTOs = userAiDao.listZsxqUsersByParams(params);
        Long totalCount = userAiDao.countZsxqUserByParams(params);
        return PageVo.build(zsxqUserInfoDTOs, req.getPageSize(), req.getPageNumber(), totalCount);
    }

    @Override
    public void operate(Long id, UserAIStatEnum operate) {
        // 根据id获取用户信息
        UserAiDO userAiDO = userAiDao.getById(id);
        // 为空则抛出异常
        if (userAiDO == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, id, "用户不存在");
        }

        // 更新用户状态
        userAiDO.setState(operate.getCode());
        userAiDao.updateById(userAiDO);
    }

    @Override
    // 加事务
    @Transactional(rollbackFor = Exception.class)
    public void update(ZsxqUserPostReq req) {
        // 根据id获取用户信息
        UserAiDO userAiDO = userAiDao.getById(req.getId());
        // 为空则抛出异常
        if (userAiDO == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, req.getId(), "用户不存在");
        }

        // 星球编号不能重复
        UserAiDO userAiDOByStarNumber = userAiDao.getByStarNumber(req.getStarNumber());
        if (userAiDOByStarNumber != null && !userAiDOByStarNumber.getId().equals(req.getId())) {
            throw ExceptionUtil.of(StatusEnum.USER_STAR_REPEAT, req.getStarNumber(), "星球编号已存在");
        }

        // 用户登录名不能重复
        UserDO userDO = userDao.getUserByUserName(req.getUserCode());
        if (userDO != null && !userDO.getId().equals(userAiDO.getUserId())) {
            throw ExceptionUtil.of(StatusEnum.USER_LOGIN_NAME_REPEAT, req.getUserCode(), "用户登录名已存在");
        }

        // 更新用户登录名
        userDO = new UserDO();
        userDO.setId(userAiDO.getUserId());
        userDO.setUserName(req.getUserCode());
        userDao.updateUser(userDO);

        // 更新用户昵称
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setId(userAiDO.getUserId());
        userInfoDO.setUserName(req.getName());
        userDao.updateById(userInfoDO);

        // 更新星球编号
        userAiDO.setStarNumber(req.getStarNumber());
        userAiDao.updateById(userAiDO);
    }

    @Override
    public void batchOperate(List<Long> ids, UserAIStatEnum operate) {
        // 批量更新用户状态
        userAiDao.batchUpdateState(ids, operate.getCode());
    }
}
