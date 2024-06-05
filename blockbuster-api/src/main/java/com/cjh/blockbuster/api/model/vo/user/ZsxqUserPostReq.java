package com.cjh.blockbuster.api.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author 沉默王二
 * @date 6/29/23
 */
@Data
public class ZsxqUserPostReq implements Serializable {
    // id
    private Long id;
    // 用户名
    private String userCode;
    // 用户昵称
    private String name;
    // 星球编号
    private String starNumber;
}
