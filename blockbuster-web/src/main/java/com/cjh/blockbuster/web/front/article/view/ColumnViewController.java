package com.cjh.blockbuster.web.front.article.view;

import com.cjh.blockbuster.api.model.context.ReqInfoContext;
import com.cjh.blockbuster.api.model.enums.column.ColumnArticleReadEnum;
import com.cjh.blockbuster.api.model.enums.column.ColumnTypeEnum;
import com.cjh.blockbuster.api.model.enums.user.UserAIStatEnum;
import com.cjh.blockbuster.api.model.vo.PageListVo;
import com.cjh.blockbuster.api.model.vo.PageParam;
import com.cjh.blockbuster.api.model.vo.article.dto.ArticleDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.ColumnArticlesDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.ColumnDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.SimpleArticleDTO;
import com.cjh.blockbuster.api.model.vo.comment.dto.TopCommentDTO;
import com.cjh.blockbuster.api.model.vo.recommend.SideBarDTO;
import com.cjh.blockbuster.core.util.MarkdownConverter;
import com.cjh.blockbuster.core.util.SpringUtil;
import com.cjh.blockbuster.service.article.repository.entity.ColumnArticleDO;
import com.cjh.blockbuster.service.article.service.ArticleReadService;
import com.cjh.blockbuster.service.article.service.ColumnService;
import com.cjh.blockbuster.service.comment.service.CommentReadService;
import com.cjh.blockbuster.service.sidebar.service.SidebarService;
import com.cjh.blockbuster.web.config.GlobalViewConfig;
import com.cjh.blockbuster.web.front.article.vo.ColumnVo;
import com.cjh.blockbuster.web.global.SeoInjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 专栏入口
 *
 * @author YiHui
 * @date 2022/9/15
 */
@Controller
@RequestMapping(path = "column")
public class ColumnViewController {
    @Autowired
    private ColumnService columnService;
    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private SidebarService sidebarService;

    /**
     * 专栏主页，展示专栏列表
     *
     * @param model
     * @return
     */
    @GetMapping(path = {"list", "/", "", "home"})
    public String list(Model model) {
        PageListVo<ColumnDTO> columns = columnService.listColumn(PageParam.newPageInstance());
        List<SideBarDTO> sidebars = sidebarService.queryColumnSidebarList();
        ColumnVo vo = new ColumnVo();
        vo.setColumns(columns);
        vo.setSideBarItems(sidebars);
        model.addAttribute("vo", vo);
        return "views/column-home/index";
    }

    /**
     * 专栏详情
     *
     * @param columnId
     * @return
     */
    @GetMapping(path = "{columnId}")
    public String column(@PathVariable("columnId") Long columnId, Model model) {
        ColumnDTO dto = columnService.queryColumnInfo(columnId);
        model.addAttribute("vo", dto);
        return "/views/column-index/index";
    }


    /**
     * 专栏的文章阅读界面
     *
     * @param columnId 专栏id
     * @param section  节数，从1开始
     * @param model
     * @return
     */
    @GetMapping(path = "{columnId}/{section}")
    public String articles(@PathVariable("columnId") Long columnId, @PathVariable("section") Integer section, Model model) {
        if (section <= 0) section = 1;
        // 查询专栏
        ColumnDTO column = columnService.queryBasicColumnInfo(columnId);

        ColumnArticleDO columnArticle = columnService.queryColumnArticle(columnId, section);
        Long articleId = columnArticle.getArticleId();
        // 文章信息
        ArticleDTO articleDTO = articleReadService.queryFullArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());
        // 返回html格式的文档内容
        articleDTO.setContent(MarkdownConverter.markdownToHtml(articleDTO.getContent()));
        // 评论信息
        List<TopCommentDTO> comments = commentReadService.getArticleComments(articleId, PageParam.newPageInstance());

        // 热门评论
        TopCommentDTO hotComment = commentReadService.queryHotComment(articleId);

        // 文章列表
        List<SimpleArticleDTO> articles = columnService.queryColumnArticles(columnId);

        ColumnArticlesDTO vo = new ColumnArticlesDTO();
        updateReadType(vo, column, articleDTO, ColumnArticleReadEnum.valueOf(columnArticle.getReadType()));
        vo.setArticle(articleDTO);
        vo.setComments(comments);
        vo.setHotComment(hotComment);
        vo.setColumn(columnId);
        vo.setSection(section);
        vo.setArticleList(articles);
        model.addAttribute("vo", vo);

        SpringUtil.getBean(SeoInjectService.class).initColumnSeo(vo, column);
        return "views/column-detail/index";
    }

    /**
     * 对于要求登录阅读的文章进行进行处理
     *
     * @param vo
     * @param column
     * @param articleDTO
     */
    private void updateReadType(ColumnArticlesDTO vo, ColumnDTO column, ArticleDTO articleDTO, ColumnArticleReadEnum articleReadEnum) {
        Long loginUser = ReqInfoContext.getReqInfo().getUserId();
        if (loginUser != null && loginUser.equals(articleDTO.getAuthor())) {
            vo.setReadType(ColumnTypeEnum.FREE.getType());
            return;
        }

        if (articleReadEnum == ColumnArticleReadEnum.COLUMN_TYPE) {
            // 专栏中的文章，没有特殊指定时，直接沿用专栏的规则
            if (column.getType() == ColumnTypeEnum.TIME_FREE.getType()) {
                long now = System.currentTimeMillis();
                if (now > column.getFreeEndTime() || now < column.getFreeStartTime()) {
                    vo.setReadType(ColumnTypeEnum.LOGIN.getType());
                } else {
                    vo.setReadType(ColumnTypeEnum.FREE.getType());
                }
            } else {
                vo.setReadType(column.getType());
            }
        } else {
            // 直接使用文章特殊设置的规则
            vo.setReadType(articleReadEnum.getRead());
        }
        // 如果是星球 or 登录阅读时，不返回全量的文章内容
        articleDTO.setContent(trimContent(vo.getReadType(), articleDTO.getContent()));
        if (vo.getReadType() == ColumnTypeEnum.STAR_READ.getType()) {
            // 星球的文章，移除相关信息
            articleDTO.setCover(null);
        }
    }

    /**
     * 文章内容隐藏
     *
     * @param readType
     * @param content
     * @return
     */
    private String trimContent(int readType, String content) {
        if (readType == ColumnTypeEnum.STAR_READ.getType()) {
            // 判断登录用户是否绑定了星球，如果是，则直接阅读完整的专栏内容
            if (ReqInfoContext.getReqInfo().getUser() != null && ReqInfoContext.getReqInfo().getUser().getStarStatus() == UserAIStatEnum.FORMAL) {
                return content;
            }

            // 返回星球相关信息
            return MarkdownConverter.markdownToHtml(SpringUtil.getBean(GlobalViewConfig.class).getStarInfo());
        }

        if ((readType == ColumnTypeEnum.LOGIN.getType() && ReqInfoContext.getReqInfo().getUserId() == null)) {
            if (content.length() > 500) {
                content = content.substring(0, 500);
            } else if (content.length() > 256) {
                content = content.substring(0, 256);
            }
        }

        return content;
    }
}
