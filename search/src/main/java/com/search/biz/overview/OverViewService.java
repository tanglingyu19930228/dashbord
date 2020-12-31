package com.search.biz.overview;

import com.alibaba.fastjson.JSONObject;
import com.search.common.utils.BigDecimalUtils;
import com.search.common.utils.DateUtils;
import com.search.dao.SysKeyDao;
import com.search.vo.DayCount;
import com.search.vo.SiteNameVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
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
public class OverViewService {

    @Autowired
    SysKeyDao sysKeyDao;

    public JSONObject parseDetailPageResponse(SearchResponse search) {
        JSONObject jsonObject = new JSONObject();
        List<DayCount> list = parseElasticsearchResponseToBean(search);
        if(CollectionUtils.isEmpty(list)){
            return new JSONObject();
        }
        jsonObject.put("totalTrend",list);
        Map<String,Long> map = new HashMap<>();
        Long total = 0L;
        Long sumAll = 0L;
        Long wxArticle = 0L;
        Long wbArticle = 0L;
        Long newsArticle = 0L;
        Long sumWxAll = 0L;
        Long sumWbAll = 0L;
        Long sumNewsAll = 0L;
        /** 2：博客；3：论坛：4：问答 */
        Long blogArticle = 0L;Long commentArticle = 0L; Long quesArticle = 0L;
        Long sumBlogAll = 0L;Long sumCommentAll = 0L; Long sumQuesAll = 0L;
        for (DayCount dayCount : list) {
            final List<SiteNameVO> inner = dayCount.getList();
            for (SiteNameVO siteNameVO : inner) {
                final Long docCount = siteNameVO.getDocCount();
                final long allCount = siteNameVO.getPublisherCollectionNum()+siteNameVO.getPublisherCommentNum()+siteNameVO.getPublisherLinkNum()
                        +siteNameVO.getPublisherPv()+siteNameVO.getPublisherRepostNum();
                total += docCount;
                sumAll += allCount;
                final Integer mediaType = siteNameVO.getMediaType();
                if(mediaType == 0){
                    //微信互动量:    点赞，阅读
                    wxArticle += siteNameVO.getDocCount();
                    sumWxAll += siteNameVO.getPublisherLinkNum()+siteNameVO.getPublisherPv();
//                    sumWxAll += allCount;
                }else if(mediaType == 1){
                    // 微博互动量:   转发+评论+点赞
                    wbArticle += siteNameVO.getDocCount();
                    sumWbAll += siteNameVO.getPublisherRepostNum()+siteNameVO.getPublisherCommentNum()+siteNameVO.getPublisherLinkNum();
//                    sumWbAll += allCount;
                }else if(mediaType == 5 ){
                    // 新闻互动量:   评论量+阅读
                    newsArticle += siteNameVO.getDocCount();
//                    sumNewsAll += allCount;
                    sumNewsAll += siteNameVO.getPublisherCommentNum()+siteNameVO.getPublisherPv();
                }else if(mediaType == 2){
                    //博客互动量:    阅读数+评论+转发数+收藏数
                    blogArticle += siteNameVO.getDocCount();
                    sumBlogAll += siteNameVO.getPublisherLinkNum() + siteNameVO.getPublisherCommentNum() + siteNameVO.getPublisherRepostNum() + siteNameVO.getPublisherCollectionNum();
                }else if(mediaType == 3){
                    //论坛互动量:    阅读数+评论数
                    commentArticle += siteNameVO.getDocCount();
                    sumCommentAll += siteNameVO.getPublisherPv() + siteNameVO.getPublisherCommentNum();
                }else if(mediaType == 4 ){
                    // 问答互动量:   回答量+点赞+阅读
                    quesArticle += siteNameVO.getDocCount();
                    sumQuesAll += siteNameVO.getPublisherCommentNum() + siteNameVO.getPublisherLinkNum() + siteNameVO.getPublisherPv();
                }else{

                }
            }
        }
        map.put("total",total);
        map.put("sumAll",sumAll);
        map.put("wxArticle",wxArticle);
        map.put("wbArticle",wbArticle);
        map.put("newsArticle",newsArticle);
        map.put("sumWxAll",sumWxAll);
        map.put("sumWbAll",sumWbAll);
        map.put("sumNewsAll",sumNewsAll);
        jsonObject.put("banner",map);
        jsonObject.put("dayAvgTrend", BigDecimalUtils.div(total,list.size()));
        Map<String,Object> obj = this.parseVoiceResource(list,total,sumAll,list.size(),map);
        jsonObject.put("voiceResource",obj);
        return jsonObject;
    }

    private List<DayCount> parseElasticsearchResponseToBean(SearchResponse searchResponse){
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
        List<DayCount> list = new ArrayList<>();
        for (Histogram.Bucket bucket : buckets) {
            final Map<String, Aggregation> asMap = bucket.getAggregations().getAsMap();
            if(Objects.isNull(asMap)){
                continue;
            }
            ParsedStringTerms longTerms = (ParsedStringTerms) asMap.get("mediaType");
            DayCount dayCount = new DayCount();
            final String keyAsString = bucket.getKeyAsString();
            final Date date = DateUtils.transferDateHadOffset(keyAsString);
            dayCount.setDateKey(date.getTime());
            dayCount.setDocCount(bucket.getDocCount());
            List<SiteNameVO> siteNameVOList = transferOneBucketToSiteNameVO(longTerms.getBuckets());
            Long oneTotal = siteNameVOList.stream().map(item->item.getPublisherCollectionNum()+item.getPublisherCommentNum()+item.getPublisherLinkNum()
                    +item.getPublisherPv()+item.getPublisherRepostNum()).collect(Collectors.summarizingLong(Long::longValue)).getSum();
            dayCount.setList(siteNameVOList);
            dayCount.setTotal(oneTotal);
            list.add(dayCount);
        }
        return list;
    }
    private List<SiteNameVO> transferOneBucketToSiteNameVO(List<? extends Terms.Bucket> buckets) {
        List<SiteNameVO> list = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {

            final long docCount = bucket.getDocCount();
            final Map<String, Aggregation> asMap = bucket.getAggregations().getAsMap();
            ParsedStringTerms parsedStringTerms = (ParsedStringTerms) asMap.get("publisherSiteName");
            final List<? extends Terms.Bucket> buckets1 = parsedStringTerms.getBuckets();
            for (Terms.Bucket bucket1 : buckets1) {
                SiteNameVO siteNameVO = new SiteNameVO();
                siteNameVO.setDocCount(bucket1.getDocCount());
                siteNameVO.setMediaType(Integer.valueOf(bucket.getKey()+""));
                siteNameVO.setName(String.valueOf(bucket1.getKey()));
                final Aggregations aggregations = bucket1.getAggregations();
                final List<Aggregation> aggregations1 = aggregations.asList();
                for (Aggregation aggregation : aggregations1) {
                    ParsedStats parsedStats = (ParsedStats) aggregation;
                    final String round = BigDecimalUtils.divRound2(parsedStats.getSum(),1, 0);
                    if(parsedStats.getName().equals("publisherLinkNum")){
                        siteNameVO.setPublisherLinkNum(Long.parseLong(round));
                    }
                    if(parsedStats.getName().equals("publisherPv")){
                        siteNameVO.setPublisherPv(Long.parseLong(round));
                    }
                    if(parsedStats.getName().equals("publisherCommentNum")){
                        siteNameVO.setPublisherCommentNum(Long.parseLong(round));
                    }
                    if(parsedStats.getName().equals("publisherCollectionNum")){
                        siteNameVO.setPublisherCollectionNum(Long.parseLong(round));
                    }
                    if(parsedStats.getName().equals("publisherRepostNum")){
                        siteNameVO.setPublisherRepostNum(Long.parseLong(round));
                    }
                }
                list.add(siteNameVO);
            }
        }
        return list;
    }
    private Map<String, Object> parseVoiceResource(List<DayCount> list, Long total, Long sumAll, int size,Map<String,Long> longMap) {
        Map<String, Object> map = new HashMap<>(8);

        Map<String, Object> voiceResourceNews = new HashMap<>(8);
        voiceResourceNews.put("newsVoiceNumber",longMap.get("newsArticle"));
        voiceResourceNews.put("newsVoice",BigDecimalUtils.divRound2(longMap.get("newsArticle"),total,4));
        voiceResourceNews.put("newsVoiceTotal",BigDecimalUtils.divRound2(longMap.get("sumNewsAll"),sumAll,4));
        voiceResourceNews.put("newsVoiceNumberTotal",longMap.get("sumNewsAll"));
        Map<String, Object> voiceResourceOthers = new HashMap<>(8);
        Long otherNumber = total-longMap.get("newsArticle")-longMap.get("wbArticle")-longMap.get("wxArticle");
        voiceResourceOthers.put("otherVoiceNumber",otherNumber);
        voiceResourceOthers.put("otherVoice",BigDecimalUtils.divRound2(otherNumber,total,4));
        Long otherNumberAll = sumAll-longMap.get("sumNewsAll")-longMap.get("sumWbAll")-longMap.get("sumWxAll");
        voiceResourceOthers.put("otherVoiceNumberTotal",otherNumberAll);
        voiceResourceOthers.put("wbVoiceTotal",BigDecimalUtils.divRound2(otherNumberAll,sumAll,4));
        Map<String, Object> voiceResourceWb = new HashMap<>(8);
        voiceResourceWb.put("newsVoiceNumber",longMap.get("wbArticle"));
        voiceResourceWb.put("wbVoice",BigDecimalUtils.divRound2(longMap.get("wbArticle"),total,4));
        voiceResourceWb.put("wbVoiceTotal",BigDecimalUtils.divRound2(longMap.get("sumWbAll"),sumAll,4));
        voiceResourceWb.put("newsVoiceNumberTotal",longMap.get("sumWbAll"));
        Map<String, Object> voiceResourceWx = new HashMap<>(8);
        voiceResourceWx.put("newsVoiceNumber",longMap.get("wxArticle"));
        voiceResourceWx.put("wxVoice",BigDecimalUtils.divRound2(longMap.get("wxArticle"),total,4));
        voiceResourceWx.put("wxVoiceTotal",BigDecimalUtils.divRound2(longMap.get("sumWxAll"),sumAll,4));
        voiceResourceWx.put("newsVoiceNumberTotal",longMap.get("sumWxAll"));
        List<SiteNameVO> all = new ArrayList<>();

        for (DayCount dayCount : list) {
            all.addAll(dayCount.getList());
        }
        Map<Integer, List<SiteNameVO>> collect = all.stream().collect(Collectors.groupingBy(SiteNameVO::getMediaType));
        List<Map<String,Object>> mapListWx = new ArrayList<>();
        List<Map<String,Object>> mapListWb = new ArrayList<>();
        List<Map<String,Object>> mapListNews = new ArrayList<>();
        List<Map<String,Object>> mapListOther = new ArrayList<>();
        List<Map<String,Object>> mapListWxTotal = new ArrayList<>();
        List<Map<String,Object>> mapListWbTotal = new ArrayList<>();
        List<Map<String,Object>> mapListNewsTotal = new ArrayList<>();
        List<Map<String,Object>> mapListOtherTotal = new ArrayList<>();

        List<SiteNameVO> otherTemp = new ArrayList<>();
        for (Map.Entry<Integer, List<SiteNameVO>> entry:collect.entrySet()) {
            Map<String,Object> temp = new HashMap<>();
            Map<String,Object> tempTotal = new HashMap<>();
            if(CollectionUtils.isEmpty(entry.getValue())){
                continue;
            }
            Map<String, List<SiteNameVO>> collect1 = entry.getValue().stream().collect(Collectors.groupingBy(SiteNameVO::getName));
            final Integer key = entry.getKey();
            if(key == 0){
                for (Map.Entry<String, List<SiteNameVO>> innerEntry : collect1.entrySet()) {
                    temp.put("name",innerEntry.getKey());
                    tempTotal.put("name",innerEntry.getKey());
                    final List<SiteNameVO> value = innerEntry.getValue();
                    Long innerTotal = 0L;
                    fixMap(temp, tempTotal, value, innerTotal);
                    mapListWx.add(temp);
                    mapListWxTotal.add(tempTotal);
                }
            }else if(key == 1){
                for (Map.Entry<String, List<SiteNameVO>> innerEntry : collect1.entrySet()) {
                    temp.put("name",innerEntry.getKey());
                    tempTotal.put("name",innerEntry.getKey());
                    final List<SiteNameVO> value = innerEntry.getValue();
                    Long innerTotal = 0L;
                    fixMap(temp, tempTotal, value, innerTotal);
                    mapListWb.add(temp);
                    mapListWbTotal.add(tempTotal);
                }
            }else if(key == 5){
                for (Map.Entry<String, List<SiteNameVO>> innerEntry : collect1.entrySet()) {
                    temp.put("name",innerEntry.getKey());
                    tempTotal.put("name",innerEntry.getKey());
                    final List<SiteNameVO> value = innerEntry.getValue();
                    Long innerTotal = 0L;
                    fixMap(temp, tempTotal, value, innerTotal);
                    mapListNews.add(temp);
                    mapListNewsTotal.add(tempTotal);
                }
            }else {
                otherTemp.addAll(entry.getValue());
            }
        }
        final Map<String, List<SiteNameVO>> collect1 = otherTemp.stream().collect(Collectors.groupingBy(SiteNameVO::getName));
        for (Map.Entry<String, List<SiteNameVO>> innerEntry : collect1.entrySet()) {
            Map<String,Object> temp = new HashMap<>();
            Map<String,Object> tempTotal = new HashMap<>();
            temp.put("name",innerEntry.getKey());
            tempTotal.put("name",innerEntry.getKey());
            final List<SiteNameVO> value = innerEntry.getValue();
            Long innerTotal = 0L;
            fixMap(temp, tempTotal, value, innerTotal);
            mapListOther.add(temp);
            mapListOtherTotal.add(tempTotal);
        }
        voiceResourceNews.put("secondNewsLevelAgg",mapListNews);
        voiceResourceNews.put("secondNewsLevelAggTotal",mapListNewsTotal);
        voiceResourceWb.put("secondWbLevelAgg",mapListWb);
        voiceResourceWb.put("secondWbLevelAggTotal",mapListWbTotal);
        voiceResourceWx.put("secondWxLevelAgg",mapListWx);
        voiceResourceWx.put("secondWxLevelAggTotal",mapListWxTotal);
        voiceResourceOthers.put("secondOthersLevelAgg",mapListOther);
        voiceResourceOthers.put("secondOthersLevelAggTotal",mapListOtherTotal);
        map.put("voiceResourceNews",voiceResourceNews);
        map.put("voiceResourceOthers",voiceResourceOthers);
        map.put("voiceResourceWb",voiceResourceWb);
        map.put("voiceResourceWx",voiceResourceWx);
        return map;
    }
    private void fixMap(Map<String, Object> temp, Map<String, Object> tempTotal, List<SiteNameVO> value, Long innerTotal) {
        if(CollectionUtils.isEmpty(value)){
            temp.put("size",0L);
            tempTotal.put("size",0L);
        }else{
            temp.put("size", value.size());
            for (SiteNameVO siteNameVO : value) {
                innerTotal += siteNameVO.getPublisherCollectionNum()+siteNameVO.getPublisherCommentNum()+siteNameVO.getPublisherLinkNum()
                        +siteNameVO.getPublisherPv()+siteNameVO.getPublisherRepostNum();
            }
            tempTotal.put("size", innerTotal);
        }
    }
}
