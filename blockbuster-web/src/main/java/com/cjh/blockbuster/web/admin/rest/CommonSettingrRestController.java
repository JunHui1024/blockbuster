package com.cjh.blockbuster.web.admin.rest;

import com.cjh.blockbuster.api.model.vo.ResVo;
import com.cjh.blockbuster.core.permission.Permission;
import com.cjh.blockbuster.core.permission.UserRole;
import com.cjh.blockbuster.service.config.service.DictCommonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Banner后台
 *
 * @author LouZai
 * @date 2022/9/19
 */
@RestController
@Permission(role = UserRole.LOGIN)
@Api(value = "通用接口管理控制器", tags = "全局设置")
@RequestMapping(path = {"api/admin/common/", "admin/common/"})
public class CommonSettingrRestController {

    @Autowired
    private DictCommonService dictCommonService;

    @ResponseBody
    @GetMapping(path = "/dict")
    public ResVo<Map<String, Object>> list() {
        Map<String, Object> bannerDTOPageVo = dictCommonService.getDict();
        return ResVo.ok(bannerDTOPageVo);
    }
}
