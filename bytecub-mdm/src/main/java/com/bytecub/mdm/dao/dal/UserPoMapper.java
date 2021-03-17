package com.bytecub.mdm.dao.dal;


import com.bytecub.mdm.core.BaseDao;
import com.bytecub.mdm.dao.po.ResourcePo;
import com.bytecub.mdm.dao.po.UserPo;

import java.util.List;

public interface UserPoMapper extends BaseDao<UserPo> {

  //  int insertSelective(UserPo record);

//    UserPo selectByPrimaryKey(Long id);

 //   int updateByPrimaryKeySelective(UserPo record);
    List<UserPo> selectByUnion(UserPo query);
}
