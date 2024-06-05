package com.cjh.blockbuster.web.front.article.vo;

import com.cjh.blockbuster.api.model.vo.PageListVo;
import com.cjh.blockbuster.api.model.vo.article.dto.ArticleDTO;
import lombok.Data;

/**
 * @author YiHui
 * @date 2022/10/28
 */
@Data
public class ArticleListVo {
    /**
     * 归档类型
     */
    private String archives;
    /**
     * 归档id
     */
    private Long archiveId;

    private PageListVo<ArticleDTO> articles;
}
