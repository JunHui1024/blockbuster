
package com.cjh.blockbuster.web.front.article.rest;

import com.cjh.blockbuster.api.model.vo.NextPageHtmlVo;
import com.cjh.blockbuster.api.model.vo.PageListVo;
import com.cjh.blockbuster.api.model.vo.PageParam;
import com.cjh.blockbuster.api.model.vo.ResVo;
import com.cjh.blockbuster.api.model.vo.article.dto.ArticleDTO;
import com.cjh.blockbuster.service.article.service.ArticleReadService;
import com.cjh.blockbuster.web.component.TemplateEngineHelper;
import com.cjh.blockbuster.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章列表
 *
 * @author yihui
 */
@RequestMapping(path = "article/api/list")
@RestController
public class ArticleListRestController extends BaseViewController {
    @Autowired
    private ArticleReadService articleService;
    @Autowired
    private TemplateEngineHelper templateEngineHelper;

    /**
     * 分类下的文章列表
     *
     * @param categoryId
     * @return
     */
    @GetMapping(path = "category/{category}")
    public ResVo<NextPageHtmlVo> categoryList(@PathVariable("category") Long categoryId,
                                              @RequestParam(name = "page") Long page,
                                              @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageListVo<ArticleDTO> list = articleService.queryArticlesByCategory(categoryId, pageParam);
        String html = templateEngineHelper.renderToVo("views/article-category-list/article/list", "articles", list);
        return ResVo.ok(new NextPageHtmlVo(html, list.getHasMore()));
    }

    /**
     * 标签下的文章列表
     *
     * @param tagId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(path = "tag/{tag}")
    public ResVo<NextPageHtmlVo> tagList(@PathVariable("tag") Long tagId,
                                         @RequestParam(name = "page") Long page,
                                         @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageListVo<ArticleDTO> list = articleService.queryArticlesByTag(tagId, pageParam);
        String html = templateEngineHelper.renderToVo("views/article-tag-list/article/list", "articles", list);
        return ResVo.ok(new NextPageHtmlVo(html, list.getHasMore()));
    }
}
