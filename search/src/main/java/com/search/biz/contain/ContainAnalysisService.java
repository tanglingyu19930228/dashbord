package com.search.biz.contain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.search.common.utils.BigDecimalUtils;
import com.search.common.utils.DateUtils;
import com.search.common.utils.StringUtils;
import com.search.dao.SysKeyDao;
import com.search.entity.SysTopicEntity;
import com.search.service.ISysTopicService;
import com.search.vo.ContainAnalysisVO;
import com.search.vo.DayCount;
import com.search.vo.EmotionAggVO;
import com.search.vo.TopicAggVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class ContainAnalysisService {

    @Autowired
    SysKeyDao sysKeyDao;
    @Autowired
    ISysTopicService sysTopicService;

    private Map<Integer,String> getTopicName(){
        final List<SysTopicEntity> sysTopicEntities = this.sysTopicService.selectSysTopicList(new SysTopicEntity());
        Map<Integer,String> map = new HashMap<>(16);
        for (SysTopicEntity sysTopicEntity : sysTopicEntities) {
            map.put(sysTopicEntity.getTopicId(),sysTopicEntity.getTopicName());
        }
        return map;
    }

    public JSONObject parseAnalysisPageResponse(SearchResponse search) {
        JSONObject obj = new JSONObject();
        try {
            final List<ContainAnalysisVO> list = convertResponseBean(search);
            if(CollectionUtils.isEmpty(list)){
                obj.put("msg","请求数据为空");
                return obj;
            }
            return calculateResult(list,getTopicName());
        } catch (Exception e) {
            log.error("解析返回集失败",e);
            obj.put("msg","解析返回集失败");
            return obj;
        }
    }

    private JSONObject calculateResult(List<ContainAnalysisVO> list, Map<Integer, String> topicName) {
        JSONObject object = new JSONObject();
        List<TopicAggVO> topicAllList = new ArrayList<>();
        List<EmotionAggVO> emotionAllList = new ArrayList<>();
        long docAll = 0L;
        for (ContainAnalysisVO item : list) {
            topicAllList.addAll(item.getTopicAggList());
            emotionAllList.addAll(item.getEmotionAggList());
            docAll += item.getDocCount();
        }
        object.put("countAll",docAll);
        calculateEmotion(object,emotionAllList);
        calculateTopic(object,topicAllList,topicName);
        return object;
    }

    private void calculateTopic(JSONObject object, List<TopicAggVO> topicAggVOList, Map<Integer, String> topicName) {
        final Map<String, List<TopicAggVO>> collect = topicAggVOList.stream().collect(Collectors.groupingBy(TopicAggVO::getKey));
        List<Map<String,String>> list = new ArrayList<>();
        final long countAll = object.getLongValue("countAll");
        extractSameCode(topicName, collect, list, countAll);
        object.put("emotionPic",list);

        final Map<Long, List<TopicAggVO>> dateCollect = topicAggVOList.stream().collect(Collectors.groupingBy(TopicAggVO::getDateTime));
        List<List<Map<String,String>>> byDayList = new ArrayList<>();
        for ( Map.Entry<Long, List<TopicAggVO>> entry:dateCollect.entrySet()) {
            final List<TopicAggVO> value = entry.getValue();
            if(CollectionUtils.isEmpty(value)){
                continue;
            }
            List<Map<String,String>> temp = new ArrayList<>();
            final long sum = value.stream().map(TopicAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            final Map<String, List<TopicAggVO>> collect1 = value.stream().collect(Collectors.groupingBy(TopicAggVO::getKey));
            extractSameCode(topicName,collect1,temp,sum);
            byDayList.add(temp);
        }
        object.put("emotionTable",byDayList);
    }

    private void extractSameCode(Map<Integer, String> topicName, Map<String, List<TopicAggVO>> collect, List<Map<String, String>> list, long countAll) {
        for ( Map.Entry<String, List<TopicAggVO>> entry: collect.entrySet()) {
            final String s = topicName.get(Integer.parseInt(entry.getKey()));
            final List<TopicAggVO> value = entry.getValue();
            if(StringUtils.isBlank(s)||CollectionUtils.isEmpty(value)){
                continue;
            }
            Map<String,String> te = new HashMap<>();
            final long sum = value.stream().map(TopicAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            te.put("size",Objects.isNull(sum)?"0":String.valueOf(sum));
            te.put("name",s);
            te.put("percent",sum==0?"0":BigDecimalUtils.divRound2(sum, countAll,4));
            list.add(te);
        }
    }

    private void calculateEmotion(JSONObject object, List<EmotionAggVO> emotionAllList) {
        final long mid = emotionAllList.stream().filter(item->0 == item.getKey()).map(EmotionAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
        final long less = emotionAllList.stream().filter(item->1 == item.getKey()).map(EmotionAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
        final long gather = emotionAllList.stream().filter(item->2 == item.getKey()).map(EmotionAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
        Long totalEmotion = mid + less + gather;
        object.put("mid",mid);
        object.put("midCalculate", mid==0?0:BigDecimalUtils.divRound2(mid,totalEmotion,4));
        object.put("less",less);
        object.put("lessCalculate", less==0?0:BigDecimalUtils.divRound2(less,totalEmotion,4));
        object.put("gather",gather);
        object.put("gatherCalculate", less==0?0:BigDecimalUtils.divRound2(gather,totalEmotion,4));
        final Map<Long, List<EmotionAggVO>> collect = emotionAllList.stream().collect(Collectors.groupingBy(EmotionAggVO::getDate));
        Map<Long, Object> map = new HashMap<>();
        for (Map.Entry<Long, List<EmotionAggVO>> entry:collect.entrySet()) {
            final List<EmotionAggVO> value = entry.getValue();
            Map<String,Long> temp = new HashMap<>();
            if(CollectionUtils.isEmpty(value)){
                temp.put("size",0L);
                temp.put("mid",0L);
                temp.put("less",0L);
                temp.put("gather",0L);
                map.put(entry.getKey(),temp);
                continue;
            }
            final long sumInnerMid = value.stream().filter(item -> 0 == item.getKey()).map(EmotionAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            final long sumInnerLess = value.stream().filter(item -> 1 == item.getKey()).map(EmotionAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            final long sumInnerGather = value.stream().filter(item -> 2 == item.getKey()).map(EmotionAggVO::getDocCount).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            temp.put("size",(long)value.size());
            temp.put("mid",sumInnerMid);
            temp.put("less",sumInnerLess);
            temp.put("gather",sumInnerGather);
            map.put(entry.getKey(),temp);
        }
        object.put("pic",map);
    }

    private  List<ContainAnalysisVO> convertResponseBean(SearchResponse searchResponse){
        Map<String, Aggregation> aggregations = searchResponse.getAggregations().asMap();
        ParsedFilter parsedFilter = (ParsedFilter) aggregations.get("boolFilter");
        if(Objects.isNull(parsedFilter)){
            return null;
        }
        Map<String, Aggregation> filtered = parsedFilter.getAggregations().asMap();
        if(Objects.isNull(filtered)||Objects.isNull(filtered.get("insertTime"))){
            return null;
        }
        ParsedDateHistogram dateHistogram = (ParsedDateHistogram)filtered.get("insertTime");
        final List<? extends Histogram.Bucket> buckets = dateHistogram.getBuckets();
        List<ContainAnalysisVO> list = new ArrayList<>();
        for (Histogram.Bucket bucket : buckets) {
            final Map<String, Aggregation> asMap = bucket.getAggregations().getAsMap();
            if(Objects.isNull(asMap)){
                continue;
            }
            ContainAnalysisVO containAnalysisVO = new ContainAnalysisVO();
            containAnalysisVO.setDocCount(bucket.getDocCount());
            final String keyAsString = bucket.getKeyAsString();
            final Date date = DateUtils.transferDateHadOffset(keyAsString);
            containAnalysisVO.setDate(date);
            containAnalysisVO.setDateLong(date.getTime());
            ParsedStringTerms topicIdMap = (ParsedStringTerms) asMap.get("topicId");
            List<TopicAggVO> topicAggVOList = this.convertTopicList(topicIdMap,date.getTime());
            ParsedStringTerms emotionTypeMap = (ParsedStringTerms) asMap.get("emotionType");
            List<EmotionAggVO> emotionList = this.convertEmotionList(emotionTypeMap,date.getTime());
            containAnalysisVO.setEmotionAggList(emotionList);
            containAnalysisVO.setTopicAggList(topicAggVOList);
            list.add(containAnalysisVO);
        }
        return list;
    }

    private List<EmotionAggVO> convertEmotionList(ParsedStringTerms emotionTypeMap,Long dateTime) {
        final List<? extends Terms.Bucket> buckets = emotionTypeMap.getBuckets();
        List<EmotionAggVO> list = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            EmotionAggVO emotionAggVO = new EmotionAggVO();
            emotionAggVO.setKey(Integer.parseInt(bucket.getKeyAsString()));
            emotionAggVO.setDate(dateTime);
            emotionAggVO.setDocCount(bucket.getDocCount());
            list.add(emotionAggVO);
        }
        return list;
    }

    private List<TopicAggVO> convertTopicList(ParsedStringTerms topicIdMap,Long dateTime) {
        final List<? extends Terms.Bucket> buckets = topicIdMap.getBuckets();
        List<TopicAggVO> list = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            final String keyAsString = bucket.getKeyAsString();
            if(StringUtils.isBlank(keyAsString)){
                continue;
            }
            TopicAggVO topicAggVO = new TopicAggVO();
            if(keyAsString.contains(",")){
                String[] split = keyAsString.split(",");
                for (String s : split) {
                    topicAggVO.setKey(s);
                    topicAggVO.setDocCount(bucket.getDocCount());
                    topicAggVO.setDateTime(dateTime);
                    list.add(topicAggVO);
                }
            }else{
                if(keyAsString.contains(".")){
                    topicAggVO.setKey(keyAsString.substring(0,keyAsString.indexOf(".")));
                }else{
                    topicAggVO.setKey(keyAsString);
                }
                topicAggVO.setDocCount(bucket.getDocCount());
                topicAggVO.setDateTime(dateTime);
                list.add(topicAggVO);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String aa = "1.23.2312";
        if(aa.contains(".")){
            final String[] split = aa.split("\\.");
            Arrays.asList(split).forEach(System.out::println);
        }
    }

}
