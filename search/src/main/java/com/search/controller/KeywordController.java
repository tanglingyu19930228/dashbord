package com.search.controller;

import com.search.annotation.BizLog;
import com.search.common.utils.R;
import com.search.dao.SysKeyDao;
import com.search.entity.SysKeyEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/keyword")
@Slf4j
@Api(value = "KeywordController", tags = "KeywordController控制层")
public class KeywordController {

    @Autowired
    SysKeyDao sysKeyDao;

    @PostMapping(value = "/getDataByKeyword")
    @BizLog(action = "getDataByKeyword")
    @ApiOperation(value = "getDataByKeyword")
    public R getDataByKeyword(@RequestBody SysKeyEntity sysKeyEntity){
        try {
            if(Objects.isNull(sysKeyEntity)||sysKeyEntity.getKeyType() ==0){
                sysKeyEntity = new SysKeyEntity();
            }
            final List<Map<String, Object>> maps = sysKeyDao.selectKeyword(sysKeyEntity);
            return R.ok(maps);
        } catch (Exception e) {
            log.error("服务器异常",e);
            return R.error("服务器异常");
        }
    }

}
