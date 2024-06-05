package com.cjh.blockbuster.service.user.service;

import com.cjh.blockbuster.api.model.enums.user.UserAIStatEnum;
import com.cjh.blockbuster.api.model.vo.PageVo;
import com.cjh.blockbuster.api.model.vo.user.SearchZsxqUserReq;
import com.cjh.blockbuster.api.model.vo.user.ZsxqUserPostReq;
import com.cjh.blockbuster.api.model.vo.user.dto.ZsxqUserInfoDTO;

import java.util.List;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 6/29/23
 */
public interface ZsxqWhiteListService {
    PageVo<ZsxqUserInfoDTO> getList(SearchZsxqUserReq req);

    void operate(Long id, UserAIStatEnum operate);

    void update(ZsxqUserPostReq req);

    void batchOperate(List<Long> ids, UserAIStatEnum operate);
}
