package com.cjh.blockbuster.web.front.notice.rest;

import com.cjh.blockbuster.api.model.context.ReqInfoContext;
import com.cjh.blockbuster.api.model.enums.NotifyTypeEnum;
import com.cjh.blockbuster.api.model.exception.ExceptionUtil;
import com.cjh.blockbuster.api.model.vo.NextPageHtmlVo;
import com.cjh.blockbuster.api.model.vo.PageListVo;
import com.cjh.blockbuster.api.model.vo.PageParam;
import com.cjh.blockbuster.api.model.vo.ResVo;
import com.cjh.blockbuster.api.model.vo.constants.StatusEnum;
import com.cjh.blockbuster.api.model.vo.notify.dto.NotifyMsgDTO;
import com.cjh.blockbuster.core.permission.Permission;
import com.cjh.blockbuster.core.permission.UserRole;
import com.cjh.blockbuster.service.notify.service.NotifyService;
import com.cjh.blockbuster.web.component.TemplateEngineHelper;
import com.cjh.blockbuster.web.front.notice.vo.NoticeResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 消息通知
 *
 * @author louzai
 * @date : 2022/9/4 10:56
 **/
@Permission(role = UserRole.LOGIN)
@RestController
@RequestMapping(path = "notice/api")
public class NoticeRestController {
    @Autowired
    private TemplateEngineHelper templateEngineHelper;

    private NotifyService notifyService;

    public NoticeRestController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    private PageListVo<NotifyMsgDTO> listItems(String type, Long page, Long pageSize) {
        NotifyTypeEnum typeEnum = NotifyTypeEnum.typeOf(type);
        if (typeEnum == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "type" + type + "非法");
        }
        if (pageSize == null) {
            pageSize = PageParam.DEFAULT_PAGE_SIZE;
        }
        return notifyService.queryUserNotices(ReqInfoContext.getReqInfo().getUserId(),
                typeEnum, PageParam.newPageInstance(page, pageSize));

    }

    /**
     * 消息通知列表，用于前后端分离的场景
     *
     * @param type     @link NotifyTypeEnum
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "list")
    public ResVo<PageListVo<NotifyMsgDTO>> list(@RequestParam(name = "type") String type,
                                                @RequestParam("page") Long page,
                                                @RequestParam(name = "pageSize", required = false) Long pageSize) {
        return ResVo.ok(listItems(type, page, pageSize));
    }

    /**
     * 返回渲染好的分页信息
     *
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "items")
    public ResVo<NextPageHtmlVo> listForView(@RequestParam(name = "type") String type,
                                       @RequestParam("page") Long page,
                                       @RequestParam(name = "pageSize", required = false) Long pageSize) {
        type = type.toLowerCase().trim();
        PageListVo<NotifyMsgDTO> list = listItems(type, page, pageSize);
        NoticeResVo vo = new NoticeResVo();
        vo.setList(list);
        vo.setSelectType(type);
        String html = templateEngineHelper.render("views/notice/tab/notify-" + type, vo);
        return ResVo.ok(new NextPageHtmlVo(html, list.getHasMore()));
    }
}
