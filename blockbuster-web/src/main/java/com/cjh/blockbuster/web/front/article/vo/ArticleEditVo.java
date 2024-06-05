package com.cjh.blockbuster.web.front.article.vo;

import com.cjh.blockbuster.api.model.vo.article.dto.ArticleDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.CategoryDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.TagDTO;
import lombok.Data;

import java.util.List;

/**
 * @author YiHui
 * @date 2022/9/2
 */
@Data
public class ArticleEditVo {

    private ArticleDTO article;

    private List<CategoryDTO> categories;

    private List<TagDTO> tags;

}
