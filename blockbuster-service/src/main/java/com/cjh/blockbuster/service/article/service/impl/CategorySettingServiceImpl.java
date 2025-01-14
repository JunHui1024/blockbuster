package com.cjh.blockbuster.service.article.service.impl;

import com.cjh.blockbuster.api.model.vo.PageVo;
import com.cjh.blockbuster.api.model.vo.article.CategoryReq;
import com.cjh.blockbuster.api.model.vo.article.SearchCategoryReq;
import com.cjh.blockbuster.api.model.vo.article.dto.CategoryDTO;
import com.cjh.blockbuster.core.util.NumUtil;
import com.cjh.blockbuster.service.article.conveter.ArticleConverter;
import com.cjh.blockbuster.service.article.conveter.CategoryStructMapper;
import com.cjh.blockbuster.service.article.repository.dao.CategoryDao;
import com.cjh.blockbuster.service.article.repository.entity.CategoryDO;
import com.cjh.blockbuster.service.article.repository.params.SearchCategoryParams;
import com.cjh.blockbuster.service.article.service.CategoryService;
import com.cjh.blockbuster.service.article.service.CategorySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类后台接口
 *
 * @author louzai
 * @date 2022-09-17
 */
@Service
public class CategorySettingServiceImpl implements CategorySettingService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void saveCategory(CategoryReq categoryReq) {
        CategoryDO categoryDO = CategoryStructMapper.INSTANCE.toDO(categoryReq);
        if (NumUtil.nullOrZero(categoryReq.getCategoryId())) {
            categoryDao.save(categoryDO);
        } else {
            categoryDO.setId(categoryReq.getCategoryId());
            categoryDao.updateById(categoryDO);
        }
        categoryService.refreshCache();
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        CategoryDO categoryDO = categoryDao.getById(categoryId);
        if (categoryDO != null){
            categoryDao.removeById(categoryDO);
        }
        categoryService.refreshCache();
    }

    @Override
    public void operateCategory(Integer categoryId, Integer pushStatus) {
        CategoryDO categoryDO = categoryDao.getById(categoryId);
        if (categoryDO != null){
            categoryDO.setStatus(pushStatus);
            categoryDao.updateById(categoryDO);
        }
        categoryService.refreshCache();
    }

    @Override
    public PageVo<CategoryDTO> getCategoryList(SearchCategoryReq req) {
        // 转换
        SearchCategoryParams params = CategoryStructMapper.INSTANCE.toSearchParams(req);
        // 查询
        List<CategoryDTO> categoryDTOS = categoryDao.listCategory(params);
        Long totalCount = categoryDao.countCategory(params);
        return PageVo.build(categoryDTOS, params.getPageSize(), params.getPageNum(), totalCount);
    }
}
