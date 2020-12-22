package com.search.biz;

import com.search.common.utils.BigDecimalUtils;
import com.search.common.utils.R;
import com.search.dao.SysOverviewCountDao;
import com.search.entity.SysContentTypeEntity;
import com.search.entity.SysEmotionTypeEntity;
import com.search.entity.SysMediaTypeEntity;
import com.search.entity.SysTopicEntity;
import com.search.service.ISysContentTypeService;
import com.search.service.ISysEmotionTypeService;
import com.search.service.ISysMediaTypeService;
import com.search.service.ISysTopicService;
import com.search.vo.Constant;
import com.search.vo.QueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class SelectValueService {

    private static final String TOPIC  = "topic";

    private static final String MEDIA_TYPE="mediaType";

    private static final String CONTENT_TYPE = "contentType";

    private static final String EMOTION_TYPE = "emotionType";

    @Autowired
    ISysTopicService sysTopicService;

    @Autowired
    ISysMediaTypeService sysMediaTypeService;

    @Autowired
    ISysContentTypeService sysContentTypeService;

    @Autowired
    ISysEmotionTypeService sysEmotionTypeService;

    @Autowired
    SysOverviewCountDao sysOverviewCountDao;

    public R getSelectValue(){

        Map<String,Object> map = new HashMap<>(8);

        try {
            List<SysTopicEntity> sysTopicEntities = sysTopicService.selectSysTopicList(new SysTopicEntity());
            map.put(TOPIC,sysTopicEntities);
            List<SysMediaTypeEntity> sysMediaTypeEntities = sysMediaTypeService.selectSysMediaTypeList(new SysMediaTypeEntity());
            map.put(MEDIA_TYPE,sysMediaTypeEntities);
            List<SysContentTypeEntity> sysContentTypeEntities = sysContentTypeService.selectSysContentTypeList(new SysContentTypeEntity());
            map.put(CONTENT_TYPE,sysContentTypeEntities);
            List<SysEmotionTypeEntity> sysEmotionTypeEntities = sysEmotionTypeService.selectSysEmotionTypeList(new SysEmotionTypeEntity());
            map.put(EMOTION_TYPE,sysEmotionTypeEntities);
            return R.ok(map);
        } catch (Exception e) {
            return R.error("服务器异常");
        }
    }


    public R getOverviewData(QueryVO queryVO){
        Map<String,Object> returnMap = new HashMap<>();
        List<Map<String, Integer>> overviewBanner = getOverviewBanner(queryVO);
        Map<String, String> voiceResource = getVoiceResource(overviewBanner);
        returnMap.put("banner",overviewBanner);
        returnMap.put("voice",voiceResource);
        return R.ok(returnMap);
    }

    /**
     * 总声量 每个平台的帖子数相加得出
     * 总互动量 每个平台(微博+微信+新闻)的互动数相加得出
     * 微信文章数
     * 微信互动量 微信文章下的点赞数+阅读数
     * 新闻数
     * 新闻互动量  新闻的评论量
     * 0：微信；1：微博；2：博客；3：论坛：4：问答；5：新闻
     * @param queryVO queryVO
     * @return Map
     */
    @Cacheable("orderByBanner")
    private Map<String,Integer> orderByBanner(QueryVO queryVO){
        List<Map<String, Integer>> overviewBanner = getOverviewBanner(queryVO);
        Integer total,totalInteraction,totalWxArticle,totalWxInteraction,totalNews,totalNewsInteraction;
        total=totalInteraction=totalWxArticle=totalWxInteraction=totalNews=totalNewsInteraction=0;
        for(Map<String, Integer> map : overviewBanner){
            total += map.get("countAll");
            totalInteraction += map.get("linkNumAll") + map.get("pvAll")+map.get("commentAll")+map.get("collectionAll")+map.get("repostNum");
            totalWxArticle += map.get("mediaType")==0?map.get("countAll"):0;
            totalWxInteraction += map.get("mediaType")==0?map.get("countAll"):0;
        }
        return null;
    }

    public Map<String,String> getVoiceResource(List<Map<String, Integer>> overviewBanner){
        final long totalAll = overviewBanner.stream().map(item -> item.get("totalAll")).collect(Collectors.summarizingLong(Integer::longValue)).getCount();
        final Map<Integer, List<Map<String, Integer>>> mediaType = overviewBanner.stream().collect(Collectors.groupingBy(item -> item.get("mediaType")));
        final long wx  = mediaType.get(0).stream().map(item -> item.get("totalAll")).collect(Collectors.summarizingLong(Integer::longValue)).getCount();
        final long wb  = mediaType.get(1).stream().map(item -> item.get("totalAll")).collect(Collectors.summarizingLong(Integer::longValue)).getCount();
        final long bk  = mediaType.get(2).stream().map(item -> item.get("totalAll")).collect(Collectors.summarizingLong(Integer::longValue)).getCount();
        final long lt  = mediaType.get(3).stream().map(item -> item.get("totalAll")).collect(Collectors.summarizingLong(Integer::longValue)).getCount();
        final long wd  = mediaType.get(4).stream().map(item -> item.get("totalAll")).collect(Collectors.summarizingLong(Integer::longValue)).getCount();
        final long news  = mediaType.get(4).stream().map(item -> item.get("totalAll")).collect(Collectors.summarizingLong(Integer::longValue)).getCount();
        Map<String,String> map = new HashMap<>(8);
        map.put("wxVoice", BigDecimalUtils.divRound2(wx,totalAll,4));
        map.put("wxVoiceNumber", String.valueOf(wx));
        map.put("wbVoice", BigDecimalUtils.divRound2(wb,totalAll,4));
        map.put("wbVoiceNumber", String.valueOf(wb));
        map.put("newsVoice", BigDecimalUtils.divRound2(news,totalAll,4));
        map.put("newsVoiceNumber", String.valueOf(news));
        long otherAll = bk +lt +wd;
        map.put("otherVoice", BigDecimalUtils.divRound2(otherAll,totalAll,4));
        map.put("otherVoiceNumber", String.valueOf(otherAll));
        return map;
    }

    @Cacheable("getOverviewBanner")
    public  List<Map<String, Integer>>getOverviewBanner(QueryVO queryVO){
        try {
            return sysOverviewCountDao.getOverviewBanner(queryVO);
        } catch (Exception e) {
            log.error("服务器异常",e);
            return new ArrayList<>();
        }
    }
}
