package com.cjh.blockbuster.web.hook.listener;

import com.cjh.blockbuster.core.util.SpringUtil;
import com.cjh.blockbuster.service.statistics.service.UserStatisticService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 通过监听session来实现实时人数统计
 *
 * @author YiHui
 * @date 2023/3/26
 */
@WebListener
public class OnlineUserCountListener implements HttpSessionListener {


    /**
     * 新增session，在线人数统计数+1
     *
     * @param se
     */
    public void sessionCreated(HttpSessionEvent se) {
        HttpSessionListener.super.sessionCreated(se);
        SpringUtil.getBean(UserStatisticService.class).incrOnlineUserCnt(1);
    }

    /**
     * session失效，在线人数统计数-1
     *
     * @param se
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSessionListener.super.sessionDestroyed(se);
        SpringUtil.getBean(UserStatisticService.class).incrOnlineUserCnt(-1);
    }
}
