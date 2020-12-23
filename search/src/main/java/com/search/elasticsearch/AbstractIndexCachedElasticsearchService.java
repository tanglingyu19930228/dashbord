package com.search.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 具有索引缓存功能的Elasticsearch操作服务类
 */
@Slf4j
public abstract class AbstractIndexCachedElasticsearchService extends AbstractElasticsearchService {
    private Set<String> indexCache = new CopyOnWriteArraySet<>();

    public AbstractIndexCachedElasticsearchService(RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
        initializeIndexCache();
    }

    @Override
    public void createIndex() {
        // initialize index cache
        initializeIndexCache();

        // judge index is inside indexCache.
        if (indexCache.contains(physicalIndex())) {
            return;
        }

        // real create index and update cache
        super.createIndex();
        indexCache.add(physicalIndex());
    }

    protected void initializeIndexCache() {
        if (indexCache.isEmpty()) {
            List<String> indices = getIndicesByAlias(logicIndex());
            indices.stream().forEach(index -> indexCache.add(index));
        }
    }

    /**
     * 指定开始和结束时间从缓存中获取索引列表，时间格式为yyyyMM
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 返回在时间区间内的索引列表
     */
    public List<String> getIndexByTime(int start, int end) {
        // 这里做索引缓存的初始化是防止系统启动时没有将索引加载到缓存的情况，导致查询无数据的情况
        initializeIndexCache();
        List<String> matchedIndexList = indexCache.stream().
                filter(index -> {
                    Integer indexNumPart = Integer.parseInt(index.substring(index.lastIndexOf(UNDER_LINE) + 1));
                    return indexNumPart >= start && indexNumPart <= end;
                }).collect(Collectors.toList());
        return matchedIndexList;
    }

}
