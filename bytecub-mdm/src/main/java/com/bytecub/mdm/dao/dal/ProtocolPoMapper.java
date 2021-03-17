package com.bytecub.mdm.dao.dal;

import com.bytecub.mdm.core.BaseDao;
import com.bytecub.mdm.dao.po.ProtocolPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProtocolPoMapper extends BaseDao<ProtocolPo> {
//    int deleteByPrimaryKey(Long id);

  //  int insert(ProtocolPo record);

  //  int insertSelective(ProtocolPo record);

//    ProtocolPo selectByPrimaryKey(Long id);

  //  int updateByPrimaryKeySelective(ProtocolPo record);


    List<ProtocolPo> queryByUnion(ProtocolPo query);
    List<ProtocolPo> listAll(@Param("status") Integer status, @Param("delFlag") Integer delFlag);
}
