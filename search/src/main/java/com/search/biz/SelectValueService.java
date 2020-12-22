package com.search.biz;

import com.search.common.utils.BigDecimalUtils;
import com.search.common.utils.R;
import com.search.dao.SysKeyDao;
import com.search.dao.SysOverviewCountDao;
import com.search.entity.*;
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
    @Autowired
    SysKeyDao sysKeyDao;

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
        List<SumVoiceResp> overviewBanner = getOverviewBanner(queryVO);
        returnMap.put("banner",renderBannerData(overviewBanner));
        returnMap.put("voiceResource",getVoiceResource(overviewBanner));
        returnMap.put("sysKey",sysKeyDao.selectKeyword());
        return R.ok(returnMap);
    }

    private Map<String,Object> renderBannerData(List<SumVoiceResp> overviewBanner) {
        Map<String,Object> map = new HashMap<>();
        try {
            final long sum = overviewBanner.stream().map(SumVoiceResp::getTotal).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            map.put("total",sum);
            final long sumAll = overviewBanner.stream().map(item->{
                return item.getLinkNumAll()+item.getPvAll()+item.getCommentAll()+item.getCollectionAll()+item.getRepostNum();
            }).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            map.put("sumAll",sumAll);
            final Map<Integer, List<SumVoiceResp>> collect = overviewBanner.stream().collect(Collectors.groupingBy(SumVoiceResp::getMediaType));
            final List<SumVoiceResp> sumVoiceRespWx = collect.get(0);
            final long sumWx = sumVoiceRespWx.stream().map(SumVoiceResp::getTotal).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            map.put("wxArticle",sumWx);
            final long sumWxAll = sumVoiceRespWx.stream().map(item -> {
                return item.getLinkNumAll() + item.getPvAll() + item.getCommentAll() + item.getCollectionAll() + item.getRepostNum();
            }).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            map.put("sumWxAll",sumWxAll);

            final List<SumVoiceResp> sumVoiceRespNews = collect.get(5);
            final long sumNews = sumVoiceRespNews.stream().map(SumVoiceResp::getTotal).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            map.put("NewsArticle",sumNews);
            final long sumNewsAll = sumVoiceRespNews.stream().map(item -> {
                return item.getLinkNumAll() + item.getPvAll() + item.getCommentAll() + item.getCollectionAll() + item.getRepostNum();
            }).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            map.put("sumNewsAll",sumNewsAll);
            return map;
        } catch (Exception e) {
            log.error("查询banner 异常",e);
            return new HashMap<>();
        }
    }

    public Map<String,Object> getVoiceResource(List<SumVoiceResp> overviewBanner){
        Map<String,Object> send = new HashMap<>(8);
        try {
            final long totalAll = overviewBanner.stream().map(SumVoiceResp::getTotal).collect(Collectors.summarizingLong(Long::valueOf)).getSum();
            long otherAll = 0;
            Map<Integer, List<SumVoiceResp>> collect = overviewBanner.stream().collect(Collectors.groupingBy(SumVoiceResp::getMediaType));
            List<SumVoiceResp> otherList = new ArrayList<>();
            for (Map.Entry<Integer, List<SumVoiceResp>> entry: collect.entrySet()) {
                Map<String,Object> voiceResourceMap = new HashMap<>(8);
                final Integer key = entry.getKey();
                List<SumVoiceResp> value = entry.getValue();
                final long val = value.stream().map(SumVoiceResp::getTotal).collect(Collectors.summarizingLong(Long::valueOf)).getSum();
                Map<String, List<SumVoiceResp>> collect1 = value.stream().collect(Collectors.groupingBy(SumVoiceResp::getSiteName));
                List<Map<String,Object>> innerList = new ArrayList<>();
                for (Map.Entry<String, List<SumVoiceResp>> inner:collect1.entrySet()) {
                    Map<String,Object> te = new HashMap<>();
                    te.put("name",inner.getKey());
                    te.put("size",inner.getValue().stream().map(SumVoiceResp::getTotal).collect(Collectors.summarizingLong(Long::longValue)).getSum());
                    innerList.add(te);
                }
                if(key == 0){
                    voiceResourceMap.put("wxVoice",totalAll==0?0: BigDecimalUtils.divRound2(val,totalAll,4));
                    voiceResourceMap.put("wxVoiceNumber", val);
                    voiceResourceMap.put("secondWxLevelAgg",innerList);
                    send.put("voiceResourceWx",voiceResourceMap);
                }else if(key == 1){
                    voiceResourceMap.put("wbVoice", totalAll==0?0:BigDecimalUtils.divRound2(val,totalAll,4));
                    voiceResourceMap.put("wbVoiceNumber",val);
                    voiceResourceMap.put("secondWbLevelAgg",collect1);
                    send.put("voiceResourceWb",innerList);
                }else if (key == 5){
                    voiceResourceMap.put("newsVoice",totalAll==0?0: BigDecimalUtils.divRound2(val,totalAll,4));
                    voiceResourceMap.put("newsVoiceNumber", val);
                    voiceResourceMap.put("secondNewsLevelAgg",collect1);
                    send.put("voiceResourceNews",innerList);
                }else {
                    otherAll += val;
                    otherList.addAll(value);
                }
            }
            Map<String,Object> otherMap = new HashMap<>();
            otherMap.put("otherVoice",totalAll==0?0: BigDecimalUtils.divRound2(otherAll,totalAll,4));
            otherMap.put("otherVoiceNumber", otherAll);
            List<Map<String,Object>> innerList = new ArrayList<>();
            for (Map.Entry<String, List<SumVoiceResp>> inner:otherList.stream().collect(Collectors.groupingBy(SumVoiceResp::getSiteName)).entrySet()) {
                Map<String,Object> te = new HashMap<>();
                te.put("name",inner.getKey());
                te.put("size",inner.getValue().stream().map(SumVoiceResp::getTotal).collect(Collectors.summarizingLong(Long::longValue)).getSum());
                innerList.add(te);
            }
            otherMap.put("secondNewsLevelAgg", innerList);
            send.put("voiceResourceOthers",otherMap);
            return send;
        } catch (Exception e) {
            log.error("查询第三层时服务器异常",e);
            return new HashMap<>();
        }
    }


    public  List<SumVoiceResp> getOverviewBanner(QueryVO queryVO){
        try {
            return sysOverviewCountDao.getOverviewBanner(queryVO);
        } catch (Exception e) {
            log.error("服务器异常",e);
            return new ArrayList<>();
        }
    }
}
