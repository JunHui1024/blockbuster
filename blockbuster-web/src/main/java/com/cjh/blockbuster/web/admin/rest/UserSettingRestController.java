package com.cjh.blockbuster.web.admin.rest;

import com.cjh.blockbuster.api.model.context.ReqInfoContext;
import com.cjh.blockbuster.api.model.vo.ResVo;
import com.cjh.blockbuster.api.model.vo.user.dto.BaseUserInfoDTO;
import com.cjh.blockbuster.api.model.vo.user.dto.SimpleUserInfoDTO;
import com.cjh.blockbuster.core.permission.Permission;
import com.cjh.blockbuster.core.permission.UserRole;
import com.cjh.blockbuster.service.user.service.UserService;
import com.cjh.blockbuster.web.front.search.vo.SearchUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户权限管理后台
 *
 * @author LouZai
 * @date 2022/9/19
 */
@RestController
@Permission(role = UserRole.ADMIN)
@Api(value = "用户管理控制器", tags = "用户管理")
@RequestMapping(path = {"api/admin/user/", "admin/user/"})
public class UserSettingRestController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户搜索")
    @GetMapping(path = "query")
    public ResVo<SearchUserVo> queryUserList(@RequestParam(name = "key", required = false) String key) {
        List<SimpleUserInfoDTO> list = userService.searchUser(key);
        SearchUserVo vo = new SearchUserVo();
        vo.setKey(key);
        vo.setItems(list);
        return ResVo.ok(vo);
    }

    @Permission(role = UserRole.LOGIN)
    @ApiOperation("获取当前登录用户信息")
    @GetMapping("info")
    public ResVo<BaseUserInfoDTO> info() {
        BaseUserInfoDTO user = ReqInfoContext.getReqInfo().getUser();
        return ResVo.ok(user);
    }
}
