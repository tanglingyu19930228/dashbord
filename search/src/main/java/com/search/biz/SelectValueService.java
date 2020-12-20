package com.search.biz;

import com.search.common.utils.R;
import com.search.entity.SysContentTypeEntity;
import com.search.entity.SysMediaTypeEntity;
import com.search.entity.SysTopicEntity;
import com.search.service.ISysContentTypeService;
import com.search.service.ISysMediaTypeService;
import com.search.service.ISysTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class SelectValueService {

    private static final String TOPIC  = "topic";

    private static final String MEDIA_TYPE="mediaType";

    private static final String CONTENT_TYPE = "contentType";

    @Autowired
    ISysTopicService sysTopicService;

    @Autowired
    ISysMediaTypeService sysMediaTypeService;

    @Autowired
    ISysContentTypeService sysContentTypeService;

    public R getSelectValue(){

        Map<String,Object> map = new HashMap<>(8);

        try {
            List<SysTopicEntity> sysTopicEntities = sysTopicService.selectSysTopicList(new SysTopicEntity());
            map.put(TOPIC,sysTopicEntities);
            List<SysMediaTypeEntity> sysMediaTypeEntities = sysMediaTypeService.selectSysMediaTypeList(new SysMediaTypeEntity());
            map.put(MEDIA_TYPE,sysMediaTypeEntities);
            List<SysContentTypeEntity> sysContentTypeEntities = sysContentTypeService.selectSysContentTypeList(new SysContentTypeEntity());
            map.put(CONTENT_TYPE,sysContentTypeEntities);
            return R.ok(map);
        } catch (Exception e) {
            return R.error("服务器异常");
        }
    }
}
