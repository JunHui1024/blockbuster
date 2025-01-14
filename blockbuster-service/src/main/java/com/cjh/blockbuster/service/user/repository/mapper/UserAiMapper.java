package com.cjh.blockbuster.service.user.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.blockbuster.api.model.vo.PageParam;
import com.cjh.blockbuster.api.model.vo.user.dto.ZsxqUserInfoDTO;
import com.cjh.blockbuster.service.user.repository.entity.UserAiDO;
import com.cjh.blockbuster.service.user.repository.params.SearchZsxqWhiteParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ai用户登录mapper接口
 *
 * @author ygl
 * @date 2022-07-18
 */
public interface UserAiMapper extends BaseMapper<UserAiDO> {

    Long countZsxqUsersByParams(@Param("searchParams") SearchZsxqWhiteParams params);

    List<ZsxqUserInfoDTO> listZsxqUsersByParams(@Param("searchParams") SearchZsxqWhiteParams params,
                                                @Param("pageParam") PageParam newPageInstance);
}
