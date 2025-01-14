package com.cjh.blockbuster.web.admin.rest;

import com.cjh.blockbuster.api.model.enums.PushStatusEnum;
import com.cjh.blockbuster.api.model.vo.PageVo;
import com.cjh.blockbuster.api.model.vo.ResVo;
import com.cjh.blockbuster.api.model.vo.article.ColumnArticleReq;
import com.cjh.blockbuster.api.model.vo.article.ColumnReq;
import com.cjh.blockbuster.api.model.vo.article.SearchColumnArticleReq;
import com.cjh.blockbuster.api.model.vo.article.SearchColumnReq;
import com.cjh.blockbuster.api.model.vo.article.dto.ColumnArticleDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.ColumnDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.SimpleColumnDTO;
import com.cjh.blockbuster.api.model.vo.constants.StatusEnum;
import com.cjh.blockbuster.core.permission.Permission;
import com.cjh.blockbuster.core.permission.UserRole;
import com.cjh.blockbuster.service.article.repository.entity.ArticleDO;
import com.cjh.blockbuster.service.article.service.ArticleReadService;
import com.cjh.blockbuster.service.article.service.ColumnSettingService;
import com.cjh.blockbuster.service.image.service.ImageService;
import com.cjh.blockbuster.web.front.search.vo.SearchColumnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专栏后台
 *
 * @author LouZai
 * @date 2022/9/19
 */
@RestController
@Slf4j
@Permission(role = UserRole.LOGIN)
@Api(value = "专栏及专栏文章管理控制器", tags = "专栏管理")
@RequestMapping(path = {"api/admin/column/", "admin/column/"})
public class ColumnSettingRestController {

    @Autowired
    private ColumnSettingService columnSettingService;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private ImageService imageService;

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "saveColumn")
    public ResVo<String> saveColumn(@RequestBody ColumnReq req) {
        columnSettingService.saveColumn(req);
        return ResVo.ok("ok");
    }

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "saveColumnArticle")
    public ResVo<String> saveColumnArticle(@RequestBody ColumnArticleReq req) {

        // 要求文章必须存在，且已经发布
        ArticleDO articleDO = articleReadService.queryBasicArticle(req.getArticleId());
        if (articleDO == null || articleDO.getStatus() == PushStatusEnum.OFFLINE.getCode()) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "教程对应的文章不存在或未发布!");
        }

        columnSettingService.saveColumnArticle(req);
        return ResVo.ok("ok");
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "deleteColumn")
    public ResVo<String> deleteColumn(@RequestParam(name = "columnId") Long columnId) {
        columnSettingService.deleteColumn(columnId);
        return ResVo.ok("ok");
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "deleteColumnArticle")
    public ResVo<String> deleteColumnArticle(@RequestParam(name = "id") Long id) {
        columnSettingService.deleteColumnArticle(id);
        return ResVo.ok("ok");
    }

    @ApiOperation("获取教程列表")
    @PostMapping(path = "list")
    public ResVo<PageVo<ColumnDTO>> list(@RequestBody SearchColumnReq req) {
        PageVo<ColumnDTO> columnDTOPageVo = columnSettingService.getColumnList(req);
        return ResVo.ok(columnDTOPageVo);
    }

    /**
     * 获取教程配套的文章列表
     * <p>
     *     请求参数有教程名、文章名
     *     返回教程配套的文章列表
     *
     * @return
     */
    @PostMapping(path = "listColumnArticle")
    public ResVo<PageVo<ColumnArticleDTO>> listColumnArticle(@RequestBody SearchColumnArticleReq req) {
        PageVo<ColumnArticleDTO> vo = columnSettingService.getColumnArticleList(req);
        return ResVo.ok(vo);
    }

    @ApiOperation("专栏搜索")
    @GetMapping(path = "query")
    public ResVo<SearchColumnVo> query(@RequestParam(name = "key", required = false) String key) {
        List<SimpleColumnDTO> list = columnSettingService.listSimpleColumnBySearchKey(key);
        SearchColumnVo vo = new SearchColumnVo();
        vo.setKey(key);
        vo.setItems(list);
        return ResVo.ok(vo);
    }
}
