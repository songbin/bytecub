package com.bytecub.mdm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bytecub.mdm.cache.IDeviceOfflineCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytecub.common.domain.dto.response.dashboard.DashBoardResDto;
import com.bytecub.common.domain.dto.response.dashboard.DashHeaderResDto;
import com.bytecub.common.domain.dto.response.dashboard.DashLineResDto;
import com.bytecub.mdm.cache.IMessageCountCache;
import com.bytecub.mdm.service.IDashBoardService;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IStatisticsService;
import com.bytecub.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: DashBoardServiceImpl.java, v 0.1 2021-01-27  Exp $$  
 */
@Service
@Slf4j
public class DashBoardServiceImpl implements IDashBoardService {
    @Autowired
    IDeviceService deviceService;
    @Autowired
    IMessageCountCache messageCountCache;
    @Autowired
    IStatisticsService statisticsService;
    @Autowired
    IDeviceOfflineCache deviceOfflineCache;
    @Override
    public DashBoardResDto index() {
        DashBoardResDto boardResDto = new DashBoardResDto();
        DashHeaderResDto headerResDto = this.dashHeader();
        boardResDto.setHeader(headerResDto);
        DashLineResDto lineResDto = this.dashLine();
        boardResDto.setLine(lineResDto);
        return boardResDto;
    }
    private DashHeaderResDto dashHeader(){
        DashHeaderResDto resDto = new DashHeaderResDto();
        resDto.setDeviceCount(deviceService.countByType(1));
        Long activeCount = deviceOfflineCache.activeCount();
        resDto.setDeviceActive(activeCount.intValue());
        resDto.setDeviceEnable(deviceService.countByType(3));
        Integer total =  messageCountCache.todayTotal()   ;
        total = null == total ? 0 : total;
        resDto.setMsgCount(total);
        return resDto;
    }
    private DashLineResDto dashLine(){

        DashLineResDto dashLineResDto = messageCountCache.dashLineRead();
        if(null != dashLineResDto){
            return dashLineResDto;
        }
        dashLineResDto = new DashLineResDto();
        Date today = new Date();
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        for(int i = 8; i > 0; i--){
            int sub = 0 - i;
            Date item = DateUtil.addDays(today, sub);
            Long count = statisticsService.countMsgTotalByDay(item);
            counts.add(count);
            String itemStr = DateUtil.getWebDateString(item);
            dates.add(itemStr);
        }
        dashLineResDto.setDates(dates);
        dashLineResDto.setCount(counts);
        messageCountCache.dashLineWrite(dashLineResDto);
        return dashLineResDto;
    }



}
