package com.cjh.blockbuster.service.article.repository.params;

import lombok.Data;

/**
 *
 * @author 沉默王二
 * @date 5/30/23
 */
@Data
public class ColumnArticleParams {
    // 教程 ID
    private Long columnId;
    // 文章 ID
    private Long articleId;
    // section
    private Integer section;
}
