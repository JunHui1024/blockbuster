package com.cjh.blockbuster.web.front.user.vo;

import com.cjh.blockbuster.api.model.enums.FollowSelectEnum;
import com.cjh.blockbuster.api.model.vo.PageListVo;
import com.cjh.blockbuster.api.model.vo.article.dto.ArticleDTO;
import com.cjh.blockbuster.api.model.vo.article.dto.TagSelectDTO;
import com.cjh.blockbuster.api.model.vo.user.dto.FollowUserInfoDTO;
import com.cjh.blockbuster.api.model.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author YiHui
 * @date 2022/9/2
 */
@Data
public class UserHomeVo {
    private String homeSelectType;
    private List<TagSelectDTO> homeSelectTags;
    /**
     * 关注列表/粉丝列表
     */
    private PageListVo<FollowUserInfoDTO> followList;
    /**
     * @see FollowSelectEnum#getCode()
     */
    private String followSelectType;
    private List<TagSelectDTO> followSelectTags;
    private UserStatisticInfoDTO userHome;

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> homeSelectList;
}
