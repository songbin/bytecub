package com.bytecub.mdm.dao.dal;

import com.bytecub.mdm.core.BaseDao;
import com.bytecub.mdm.dao.po.ProductFuncPo;

import java.util.List;

public interface ProductFuncPoMapper extends BaseDao<ProductFuncPo> {
  /**
   * 根据标识符更新数据
   * @param record ProductFuncPo
   * @return
   * */
    int updateByIdentifier(ProductFuncPo record);
    /**
     * 组合查询
     * @param record ProductFuncPo
     * @return  List
     * */
    List<ProductFuncPo> queryByUnion(ProductFuncPo record);
}
