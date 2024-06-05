package com.cjh.blockbuster.service.article.conveter;

import com.cjh.blockbuster.api.model.vo.article.CategoryReq;
import com.cjh.blockbuster.api.model.vo.article.SearchCategoryReq;
import com.cjh.blockbuster.api.model.vo.article.dto.CategoryDTO;
import com.cjh.blockbuster.service.article.repository.entity.CategoryDO;
import com.cjh.blockbuster.service.article.repository.params.SearchCategoryParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 * @author 沉默王二
 * @date 5/27/23
 */
@Mapper
public interface CategoryStructMapper {
    // instance
    CategoryStructMapper INSTANCE = Mappers.getMapper( CategoryStructMapper.class );

    // req to params
    @Mapping(source = "pageNumber", target = "pageNum")
    SearchCategoryParams toSearchParams(SearchCategoryReq req);

    // do to dto
    @Mapping(source = "id", target = "categoryId")
    @Mapping(source = "categoryName", target = "category")
    CategoryDTO toDTO(CategoryDO categoryDO);

    List<CategoryDTO> toDTOs(List<CategoryDO> list);

    // req to do
    @Mapping(source = "category", target = "categoryName")
    CategoryDO toDO(CategoryReq categoryReq);
}
