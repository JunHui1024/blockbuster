package com.cjh.blockbuster.service.article.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cjh.blockbuster.api.model.exception.ExceptionUtil;
import com.cjh.blockbuster.api.model.vo.PageParam;
import com.cjh.blockbuster.api.model.vo.PageVo;
import com.cjh.blockbuster.api.model.vo.article.ColumnArticleReq;
import com.cjh.blockbuster.api.model.vo.article.ColumnReq;
import com.cjh.blockbuster.api.model.vo.article.SearchColumnArticleReq;
import com.cjh.blockbuster.api.model.vo.article.SearchColumnReq;
import com.cjh.blockbuster.api.model.vo.article.dto.ColumnArticleDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.ColumnDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.SimpleColumnDTO;
import com.cjh.blockbuster.api.model.vo.constants.StatusEnum;
import com.cjh.blockbuster.api.model.vo.user.dto.BaseUserInfoDTO;
import com.cjh.blockbuster.core.util.NumUtil;
import com.cjh.blockbuster.service.article.conveter.ColumnArticleStructMapper;
import com.cjh.blockbuster.service.article.conveter.ColumnStructMapper;
import com.cjh.blockbuster.service.article.repository.dao.ArticleDao;
import com.cjh.blockbuster.service.article.repository.dao.ColumnArticleDao;
import com.cjh.blockbuster.service.article.repository.dao.ColumnDao;
import com.cjh.blockbuster.service.article.repository.entity.ArticleDO;
import com.cjh.blockbuster.service.article.repository.entity.ColumnArticleDO;
import com.cjh.blockbuster.service.article.repository.entity.ColumnInfoDO;
import com.cjh.blockbuster.service.article.repository.params.SearchColumnArticleParams;
import com.cjh.blockbuster.service.article.repository.params.SearchColumnParams;
import com.cjh.blockbuster.service.article.service.ColumnSettingService;
import com.cjh.blockbuster.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 专栏后台接口
 *
 * @author louzai
 * @date 2022-09-19
 */
@Service
public class ColumnSettingServiceImpl implements ColumnSettingService {

    @Autowired
    private UserService userService;

    @Autowired
    private ColumnArticleDao columnArticleDao;

    @Autowired
    private ColumnDao columnDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ColumnStructMapper columnStructMapper;

    @Override
    public void saveColumn(ColumnReq req) {
        ColumnInfoDO columnInfoDO = columnStructMapper.toDo(req);
        if (NumUtil.nullOrZero(req.getColumnId())) {
            columnDao.save(columnInfoDO);
        } else {
            columnInfoDO.setId(req.getColumnId());
            columnDao.updateById(columnInfoDO);
        }
    }


    /**
     * 将文章保存到对应的专栏中
     *
     * @param articleId
     * @param columnId
     */
    public void saveColumnArticle(Long articleId, Long columnId) {
        // 转换参数
        // 插入的时候，需要判断是否已经存在
        ColumnArticleDO exist = columnArticleDao.getOne(Wrappers.<ColumnArticleDO>lambdaQuery()
                .eq(ColumnArticleDO::getArticleId, articleId));
        if (exist != null) {
            if (!Objects.equals(columnId, exist.getColumnId())) {
                // 更新
                exist.setColumnId(columnId);
                columnArticleDao.updateById(exist);
            }
        } else {
            // 将文章保存到专栏中，章节序号+1
            ColumnArticleDO columnArticleDO = new ColumnArticleDO();
            columnArticleDO.setColumnId(columnId);
            columnArticleDO.setArticleId(articleId);
            // section 自增+1
            Integer maxSection = columnArticleDao.selectMaxSection(columnId);
            columnArticleDO.setSection(maxSection + 1);
            columnArticleDao.save(columnArticleDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveColumnArticle(ColumnArticleReq req) {
        // 转换参数
        ColumnArticleDO columnArticleDO = ColumnArticleStructMapper.INSTANCE.reqToDO(req);
        if (NumUtil.nullOrZero(columnArticleDO.getId())) {
            // 插入的时候，需要判断是否已经存在
            ColumnArticleDO exist = columnArticleDao.getOne(Wrappers.<ColumnArticleDO>lambdaQuery()
                    .eq(ColumnArticleDO::getColumnId, columnArticleDO.getColumnId())
                    .eq(ColumnArticleDO::getArticleId, columnArticleDO.getArticleId()));
            if (exist != null) {
                throw ExceptionUtil.of(StatusEnum.COLUMN_ARTICLE_EXISTS, "请勿重复添加");
            }

            // section 自增+1
            Integer maxSection = columnArticleDao.selectMaxSection(columnArticleDO.getColumnId());
            columnArticleDO.setSection(maxSection + 1);
            columnArticleDao.save(columnArticleDO);
        } else {
            columnArticleDao.updateById(columnArticleDO);
        }

        // 同时，更新 article 的 shortTitle 短标题
        if (req.getShortTitle() != null) {
            ArticleDO articleDO = new ArticleDO();
            articleDO.setShortTitle(req.getShortTitle());
            articleDO.setId(req.getArticleId());
            articleDao.updateById(articleDO);
        }
    }

    @Override
    public void deleteColumn(Long columnId) {
        columnDao.deleteColumn(columnId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteColumnArticle(Long id) {
        ColumnArticleDO columnArticleDO = columnArticleDao.getById(id);
        if (columnArticleDO != null) {
            columnArticleDao.removeById(id);
            // 删除的时候，批量更新 section，比如说原来是 1,2,3,4,5,6,7,8,9,10，删除 5，那么 6-10 的 section 都要减 1
            columnArticleDao.update(null, Wrappers.<ColumnArticleDO>lambdaUpdate()
                    .setSql("section = section - 1")
                    .eq(ColumnArticleDO::getColumnId, columnArticleDO.getColumnId())
                    // section 大于 1
                    .gt(ColumnArticleDO::getSection, 1)
                    .gt(ColumnArticleDO::getSection, columnArticleDO.getSection()));
        }
    }

    @Override
    public PageVo<ColumnDTO> getColumnList(SearchColumnReq req) {
        // 转换参数
        ColumnStructMapper mapper = ColumnStructMapper.INSTANCE;
        SearchColumnParams params = mapper.reqToSearchParams(req);
        // 查询
        List<ColumnInfoDO> columnList = columnDao.listColumnsByParams(params, PageParam.newPageInstance(req.getPageNumber(), req.getPageSize()));
        // 转属性
        List<ColumnDTO> columnDTOS = mapper.infoToDtos(columnList);

        // 进行优化，由原来的多次查询用户信息，改为一次查询用户信息
        // 获取所有需要的用户id
        // 判断 columnDTOS 是否为空
        if (CollUtil.isNotEmpty(columnDTOS)) {
            List<Long> userIds = columnDTOS.stream().map(ColumnDTO::getAuthor).collect(Collectors.toList());

            // 查询所有的用户信息
            List<BaseUserInfoDTO> users = userService.queryBasicUserInfos(userIds);

            // 创建一个id到用户信息的映射
            Map<Long, BaseUserInfoDTO> userMap = users.stream().collect(Collectors.toMap(BaseUserInfoDTO::getId, Function.identity()));

            // 设置作者信息
            columnDTOS.forEach(columnDTO -> {
                BaseUserInfoDTO user = userMap.get(columnDTO.getAuthor());
                columnDTO.setAuthorName(user.getUserName());
                columnDTO.setAuthorAvatar(user.getPhoto());
                columnDTO.setAuthorProfile(user.getProfile());
            });
        }

        Integer totalCount = columnDao.countColumnsByParams(params);
        return PageVo.build(columnDTOS, req.getPageSize(), req.getPageNumber(), totalCount);
    }

    @Override
    public PageVo<ColumnArticleDTO> getColumnArticleList(SearchColumnArticleReq req) {
        // 转换参数
        ColumnArticleStructMapper mapper = ColumnArticleStructMapper.INSTANCE;
        SearchColumnArticleParams params = mapper.toSearchParams(req);
        // 查询
        List<ColumnArticleDTO> simpleArticleDTOS = columnDao.listColumnArticlesDetail(params, PageParam.newPageInstance(req.getPageNumber(), req.getPageSize()));
        int totalCount = columnDao.countColumnArticles(params);
        return PageVo.build(simpleArticleDTOS, req.getPageSize(), req.getPageNumber(), totalCount);
    }

    @Override
    public List<SimpleColumnDTO> listSimpleColumnBySearchKey(String key) {
        LambdaQueryWrapper<ColumnInfoDO> query = Wrappers.lambdaQuery();
        query.select(ColumnInfoDO::getId, ColumnInfoDO::getColumnName, ColumnInfoDO::getCover)
                .and(!StringUtils.isEmpty(key),
                        v -> v.like(ColumnInfoDO::getColumnName, key)
                )
                .orderByDesc(ColumnInfoDO::getId);
        List<ColumnInfoDO> articleDOS = columnDao.list(query);
        return ColumnStructMapper.INSTANCE.infoToSimpleDtos(articleDOS);
    }


}
