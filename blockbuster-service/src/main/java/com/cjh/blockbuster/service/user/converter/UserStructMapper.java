package com.cjh.blockbuster.service.user.converter;

import com.cjh.blockbuster.api.model.vo.user.SearchZsxqUserReq;
import com.cjh.blockbuster.service.user.repository.params.SearchZsxqWhiteParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author 沉默王二
 * @date 6/29/23
 */
@Mapper
public interface UserStructMapper {
    UserStructMapper INSTANCE = Mappers.getMapper( UserStructMapper.class );
    // req to params
    @Mapping(source = "pageNumber", target = "pageNum")
    // state to status
    @Mapping(source = "state", target = "status")
    SearchZsxqWhiteParams toSearchParams(SearchZsxqUserReq req);
}
