package com.search.biz;

import com.alibaba.fastjson.JSONObject;
import com.search.common.utils.BigDecimalUtils;
import com.search.common.utils.DateUtils;
import com.search.vo.DayCount;
import com.search.vo.QueryVO;
import com.search.vo.SiteNameVO;
import com.search.vo.VoiceTrendVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTermsAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregator;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class OriginVoiceParseService {



    public JSONObject parseElasticsearchResponse(SearchResponse search, QueryVO queryVO) {
        JSONObject object = new JSONObject();
        List<VoiceTrendVO> list = this.parseToBean(search,queryVO);
        object.putIfAbsent("list",list);
        return object;
    }

    private List<VoiceTrendVO> parseToBean(SearchResponse searchResponse,QueryVO queryVO) {
        Map<String, Aggregation> aggregations = searchResponse.getAggregations().asMap();
        ParsedFilter parsedFilter = (ParsedFilter) aggregations.get("boolFilter");
        if(Objects.isNull(parsedFilter)){
            return null;
        }
        Map<String, Aggregation> filtered = parsedFilter.getAggregations().asMap();
        if(Objects.isNull(filtered)||Objects.isNull(filtered.get("publisherSiteName"))||Objects.isNull(filtered.get("publisher"))){
            return null;
        }
        ParsedStringTerms publisherSiteNameTerms = (ParsedStringTerms)filtered.get("publisherSiteName");
        final List<? extends Terms.Bucket> siteNameBuckets = publisherSiteNameTerms.getBuckets();
        List<VoiceTrendVO> list = new ArrayList<>();
        final Integer mediaType = queryVO.getMediaType();
        if(mediaType == 0 || mediaType == 1){
            // 微博微信按照发布人分
            ParsedStringTerms publisherTerms = (ParsedStringTerms) filtered.get("publisher");

            final List<? extends Terms.Bucket> publisherBuckets = publisherTerms.getBuckets();
            for (Terms.Bucket publisherBucket : publisherBuckets) {
                VoiceTrendVO temp = new VoiceTrendVO();
                temp.setArticleCount(publisherBucket.getDocCount());
                temp.setPublisher(publisherBucket.getKeyAsString());
                temp.setMediaType(mediaType);
                final Aggregations bucketAggregations = publisherBucket.getAggregations();
                final List<Aggregation> aggregations1 = bucketAggregations.asList();
                for (Aggregation aggregation : aggregations1) {
                    if(aggregation instanceof ParsedStats){
                        final ParsedStats cast = ParsedStats.class.cast(aggregation);
                        final String round = BigDecimalUtils.divRound2(cast.getSum(),1, 0);
                        if("publisherLinkNum".equals(cast.getName())){
                            temp.setPublisherLinkNum(Long.valueOf(round));
                        }
                        if(cast.getName().equals("publisherPv")){
                            temp.setPublisherPv(Long.parseLong(round));
                        }
                        if(cast.getName().equals("publisherCommentNum")){
                            temp.setPublisherCommentNum(Long.parseLong(round));
                        }
                        if(cast.getName().equals("publisherCollectionNum")){
                            temp.setPublisherCollectionNum(Long.parseLong(round));
                        }
                        if(cast.getName().equals("publisherRepostNum")){
                            temp.setPublisherRepostNum(Long.parseLong(round));
                        }
                    }else {
                        ParsedStringTerms  stringTerms = (ParsedStringTerms) aggregation;
                        final List<? extends Terms.Bucket> buckets = stringTerms.getBuckets();
                        if(!CollectionUtils.isEmpty(buckets)){
                            final Terms.Bucket bucket1 = buckets.get(0);
                            final String keyAsString = bucket1.getKeyAsString();
                            temp.setPublisherUrl(keyAsString);
                        }
                    }
                }
                if(mediaType ==0 ){
                    temp.setSumAllCount(temp.getPublisherLinkNum()+temp.getPublisherPv());
                }else {
                    temp.setSumAllCount(temp.getPublisherRepostNum()+temp.getPublisherCommentNum()+temp.getPublisherLinkNum());
                }
                list.add(temp);
            }
        }else {
            // 新闻以及其他按照 站点分 只 需要统计   站点名称 + 发布文章数
            for (Terms.Bucket bucket : siteNameBuckets) {
                VoiceTrendVO temp = new VoiceTrendVO();
                temp.setMediaType(mediaType);
                temp.setMediaName(bucket.getKeyAsString());
                temp.setArticleCount(bucket.getDocCount());
                temp.setSiteName(bucket.getKeyAsString());
                list.add(temp);
            }
        }
        return list;
    }
}
