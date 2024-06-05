package com.cjh.blockbuster.api.model.vo.user;

import lombok.Data;

/**
 *
 * @author 沉默王二
 * @date 6/29/23
 */
@Data
public class SearchZsxqUserReq {
    // 用户名称
    private String name;
    // 星球编号
    private String starNumber;

    private Integer state;
    // 分页
    private Long pageNumber;
    private Long pageSize;
}
