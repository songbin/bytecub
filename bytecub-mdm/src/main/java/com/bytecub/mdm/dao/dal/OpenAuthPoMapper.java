package com.bytecub.mdm.dao.dal;


import com.bytecub.mdm.core.BaseDao;
import com.bytecub.mdm.dao.po.OpenAuthPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
public interface OpenAuthPoMapper extends BaseDao<OpenAuthPo> {
    List<OpenAuthPo> selectByUnion(Page<?> page, @Param("query") OpenAuthPo record);
}
