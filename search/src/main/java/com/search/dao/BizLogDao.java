package com.search.dao;

import com.search.entity.BizLogDto;
import org.apache.ibatis.annotations.Param;

public interface BizLogDao {

    void addUserOperationLog(@Param("operationLogDto") BizLogDto operationLogDto);
}
