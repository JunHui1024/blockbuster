package com.cjh.blockbuster.service.article.conveter;

import com.cjh.blockbuster.api.model.vo.article.SearchArticleReq;
import com.cjh.blockbuster.service.article.repository.params.SearchArticleParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleStructMapper {
    ArticleStructMapper INSTANCE = Mappers.getMapper( ArticleStructMapper.class );

    @Mapping(source = "pageNumber", target = "pageNum")
    SearchArticleParams toSearchParams(SearchArticleReq req);
}
