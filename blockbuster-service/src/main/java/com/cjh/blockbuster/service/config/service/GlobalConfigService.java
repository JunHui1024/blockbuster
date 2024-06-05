package com.cjh.blockbuster.service.config.service;

import com.cjh.blockbuster.api.model.vo.PageVo;
import com.cjh.blockbuster.api.model.vo.config.GlobalConfigReq;
import com.cjh.blockbuster.api.model.vo.config.SearchGlobalConfigReq;
import com.cjh.blockbuster.api.model.vo.config.dto.GlobalConfigDTO;

/**
 *
 * @author 沉默王二
 * @date 6/30/23
 */
public interface GlobalConfigService {
    PageVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req);

    void save(GlobalConfigReq req);

    void delete(Long id);
}
