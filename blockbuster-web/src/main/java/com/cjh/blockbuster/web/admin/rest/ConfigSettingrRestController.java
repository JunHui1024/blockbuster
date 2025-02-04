package com.cjh.blockbuster.web.admin.rest;

import com.cjh.blockbuster.api.model.enums.PushStatusEnum;
import com.cjh.blockbuster.api.model.vo.PageVo;
import com.cjh.blockbuster.api.model.vo.ResVo;
import com.cjh.blockbuster.api.model.vo.banner.ConfigReq;
import com.cjh.blockbuster.api.model.vo.banner.SearchConfigReq;
import com.cjh.blockbuster.api.model.vo.banner.dto.ConfigDTO;
import com.cjh.blockbuster.api.model.vo.constants.StatusEnum;
import com.cjh.blockbuster.core.permission.Permission;
import com.cjh.blockbuster.core.permission.UserRole;
import com.cjh.blockbuster.service.config.service.impl.ConfigSettingServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Banner后台
 *
 * @author LouZai
 * @date 2022/9/19
 */
@RestController
@Permission(role = UserRole.LOGIN)
@Api(value = "后台运营配置管理控制器", tags = "配置管理")
@RequestMapping(path = {"api/admin/config/", "admin/config/"})
public class ConfigSettingrRestController {

    @Autowired
    private ConfigSettingServiceImpl configSettingService;

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody ConfigReq configReq) {
        configSettingService.saveConfig(configReq);
        return ResVo.ok("ok");
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "configId") Integer configId) {
        configSettingService.deleteConfig(configId);
        return ResVo.ok("ok");
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResVo<String> operate(@RequestParam(name = "configId") Integer configId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        configSettingService.operateConfig(configId, pushStatus);
        return ResVo.ok("ok");
    }

    /**
     * 获取配置列表
     *
     * @return
     */
    @PostMapping(path = "list")
    public ResVo<PageVo<ConfigDTO>> list(@RequestBody SearchConfigReq req) {
        PageVo<ConfigDTO> bannerDTOPageVo = configSettingService.getConfigList(req);
        return ResVo.ok(bannerDTOPageVo);
    }
}
