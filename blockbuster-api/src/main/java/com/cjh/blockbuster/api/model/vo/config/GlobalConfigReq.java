package com.cjh.blockbuster.api.model.vo.config;

import lombok.Data;

/**
 *
 * @author 沉默王二
 * @date 6/30/23
 */
@Data
public class GlobalConfigReq {
    // 配置项名称
    private String keywords;
    // 配置项值
    private String value;
    // 备注
    private String comment;
    // id
    private Long id;
}
