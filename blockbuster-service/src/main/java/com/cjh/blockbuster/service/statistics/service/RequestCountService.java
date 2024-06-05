package com.cjh.blockbuster.service.statistics.service;

import com.cjh.blockbuster.api.model.vo.statistics.dto.StatisticsDayDTO;
import com.cjh.blockbuster.service.statistics.repository.entity.RequestCountDO;

import java.util.List;

/**
 *
 * @author 沉默王二
 * @date 5/24/23
 */
public interface RequestCountService {
    RequestCountDO getRequestCount(String host);

    void insert(String host);

    void incrementCount(Long id);

    Long getPvTotalCount();

    List<StatisticsDayDTO> getPvUvDayList(Integer day);
}
