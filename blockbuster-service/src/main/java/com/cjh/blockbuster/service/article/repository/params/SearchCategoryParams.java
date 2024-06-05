package com.cjh.blockbuster.service.article.repository.params;

import com.cjh.blockbuster.api.model.vo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author 沉默王二
 * @date 5/27/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchCategoryParams extends PageParam {
    // 类目名称
    private String category;
}
