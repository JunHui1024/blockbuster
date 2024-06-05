package com.cjh.blockbuster.service.user.repository.params;

import com.cjh.blockbuster.api.model.vo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author 沉默王二
 * @date 6/29/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchZsxqWhiteParams extends PageParam {

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 星球编号
     */
    private String starNumber;

    /**
     * 登录用户名
     */
    private String name;

    /**
     * 用户编号
     */
    private String userCode;
}