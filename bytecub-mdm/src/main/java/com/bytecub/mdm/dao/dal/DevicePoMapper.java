package com.bytecub.mdm.dao.dal;

import com.bytecub.common.domain.dto.request.device.DevCreateReqDto;
import com.bytecub.common.domain.dto.request.DevQueryReqDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.mdm.core.BaseDao;
import com.bytecub.mdm.dao.po.DevicePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DevicePoMapper extends BaseDao<DevicePo> {
    /**根据编码更新设备
     * @param record
     * @return
     * */
    int updateByCode(DevicePo record);
    /**
     * 批量插入设备
     * @param batchList
     * @return
     * */
    int batchInsert(@Param("batchList") List<DevCreateReqDto> batchList);
    /**
     * 批量删除
     * @param ids
     * @return
     * */
    int batchDeleteById(List<Long> ids);
    /**
     * 批量设置不可用
     * @param ids
     * @return
     * */
    int batchDisableById(List<Long> ids);
    /**
     * 批量上线
     * @param devices
     * @return
     * */
    int batchOnlineByCode(List<String> devices);
    /**
     * 批量下线
     * @param devices
     * @return
     * */
    int batchOfflineByCode(List<String> devices);
    /**
     * 设备关联网关设备
     * @param devices
     * @param gwDevCode
     * @return int
     * */
    int mapGateway(@Param("devices") List<String> devices, @Param("gwDevCode") String gwDevCode);
    /**
     * 计算数量
     * @param dto
     * @return
     * */
    Long countByPage(@Param("item") DevQueryReqDto dto);
    /**
     * 根据ID批量激活
     * @param ids
     * @return
     * */
    int batchEnableById(List<Long> ids);
    /**
     * 根据编码搜索设备
     * @param deviceCode
     * @return
     * */
    DevicePageResDto selectByCode(String deviceCode);
    /**根据ID批量查询
     * @param ids
     * @return
     * */
    List<DevicePageResDto> selectByIds(List<Long> ids);
    /**
     * 分页查询
     * @param dto
     * @param startId
     * @param pageSize
     * @return
     * */
    List<DevicePageResDto> queryByPage(@Param("item") DevQueryReqDto dto, @Param("startId") Integer startId, @Param("size") Integer pageSize);
}
