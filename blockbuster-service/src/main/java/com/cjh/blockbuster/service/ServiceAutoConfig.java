package com.cjh.blockbuster.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author  cjh
 * @date  ${DATE} ${TIME}
 * @version 1.0
*/

@Configuration
@ComponentScan("com.cjh.blockbuster.service")
@MapperScan(basePackages = {
        "com.cjh.blockbuster.service.article.repository.mapper",
        "com.cjh.blockbuster.service.user.repository.mapper",
        "com.cjh.blockbuster.service.comment.repository.mapper",
        "com.cjh.blockbuster.service.config.repository.mapper",
        "com.cjh.blockbuster.service.statistics.repository.mapper",
        "com.cjh.blockbuster.service.notify.repository.mapper",})
public class ServiceAutoConfig {
}
