package com.cjh.blockbuster.service.config.repository.params;

import com.cjh.blockbuster.api.model.vo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author 沉默王二
 * @date 6/30/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchGlobalConfigParams extends PageParam {
    // 配置项名称
    private String key;
    // 配置项值
    private String value;
    // 备注
    private String comment;
}
