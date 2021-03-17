package com.bytecub.mdm.dao.dal;


import com.bytecub.common.domain.dto.response.ProductDetailResDto;
import com.bytecub.mdm.core.BaseDao;
import com.bytecub.mdm.dao.po.ProductPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductPoMapper extends BaseDao<ProductPo> {
   // int insertSelective(ProductPo record);

//    ProductPo selectByPrimaryKey(Long id);

 //   int updateByPrimaryKeySelective(ProductPo record);
    List<ProductPo> listAll(@Param("status") Integer status, @Param("delFlag") Integer delFlag);
    List<ProductPo> selectByUnion(ProductPo record);
    List<ProductPo> searchByName( @Param("name") String name, @Param("startId") Integer startId, @Param("size") Integer pageSize);
    Long countByName(@Param("name") String name);

    ProductDetailResDto selectByCode(String code);
}
