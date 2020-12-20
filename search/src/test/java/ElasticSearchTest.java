import com.alibaba.fastjson.JSON;
import com.search.SearchApplication;
import com.search.common.utils.DateUtils;
import com.search.entity.SysArticleEntity;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.elasticsearch.action.index.*;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.Arrays;

@SpringBootTest(classes = { SearchApplication.class })
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

    private static final String INDEX ="newindex";

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void testIndex() throws IOException {

        final GetIndexRequest newindex = new GetIndexRequest(INDEX);
        final boolean exists = restHighLevelClient.indices().exists(newindex, RequestOptions.DEFAULT);
        System.out.println(exists);

    }

    @Test
    public void testAddDoc() throws Exception{

        SysArticleEntity sysArticleEntity = generatorOne();
        IndexRequest request = new IndexRequest(INDEX);
        request.id(String.valueOf(sysArticleEntity.getId()));
        String s = JSON.toJSONString(sysArticleEntity);
        request.source(s, XContentType.JSON);

        final IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        System.out.println(index.status());
    }

    @Test
    public void testAddDocBulk() throws Exception{

        final BulkRequest bulkRequest = new BulkRequest();

        for (int i= 2;i< 10;i++) {
            SysArticleEntity sysArticleEntity = generatorOne();
            sysArticleEntity.setId(i);
            IndexRequest request = new IndexRequest(INDEX);
            bulkRequest.add(
                    request.id(String.valueOf(i)).source(JSON.toJSONString(sysArticleEntity),XContentType.JSON)
            );
        }

        final BulkResponse index = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        System.out.println(index.hasFailures());
    }

    @Test
    public void testGetLastOne() throws Exception{
        final SearchRequest searchRequest = new SearchRequest(INDEX);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0).size(1).sort("id", SortOrder.DESC);
        final MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        final SearchSourceBuilder query = searchSourceBuilder.query(matchAllQueryBuilder);
        matchAllQueryBuilder.queryName("id");
        searchRequest.source(query);
        final SearchResponse search = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println("++++++++++++++++++++++++++");
        System.out.println(JSON.toJSONString(search));
        final SearchHit documentFields = Arrays.stream(search.getHits().getHits()).findFirst().get();
        System.out.println("========="+JSON.toJSONString(documentFields));
        final Object id = documentFields.getSourceAsMap().get("id");
        System.out.println(id.toString());
    }

    private SysArticleEntity generatorOne(){
        SysArticleEntity sysArticleEntity = new SysArticleEntity();
        sysArticleEntity.setId(2);
        sysArticleEntity.setTitleId("14, 7, 2");
        sysArticleEntity.setTopicId("13,13");
        sysArticleEntity.setContentId(1);
        sysArticleEntity.setContent("今日份逛街：来一顿好久没吃的海底捞??偶遇日食记 酥饼巨可爱～没忍住买了手机壳??回购一支阿玛尼红管200??大冬天馋的喝了一杯香草星冰乐?? ?今日份逛街：来一顿好久没吃的海底捞??偶遇日食记 酥饼巨可爱～没忍住买了手机壳??回购一支阿玛尼红管200??大冬天馋的喝了一杯香草星冰乐?? ?");
        sysArticleEntity.setEmotionType(1);
        sysArticleEntity.setEmotionValue(123);
        sysArticleEntity.setInsertTime(DateUtils.getNowDate());
        sysArticleEntity.setMediaType(0);
        sysArticleEntity.setPublisher("优春娇");
        sysArticleEntity.setPublisherId(6509839334L);
        sysArticleEntity.setPublisherLinkNum(123);
        sysArticleEntity.setPublisherPv(4321);
        sysArticleEntity.setPublisherCommentNum(123);
        sysArticleEntity.setPublisherCollectionNum(123123);
        sysArticleEntity.setPublisherRepostNum(123123);
        sysArticleEntity.setPublisherSiteName("网易博客");
        sysArticleEntity.setPublisherArticleTitle("");
        sysArticleEntity.setPublisherArticleUnique("");
        sysArticleEntity.setPublisherArticleUrl("");
        sysArticleEntity.setPublisher("ce0b0d401837e2712331483dc0b611c6");
        sysArticleEntity.setPublisherUserType(1);
        sysArticleEntity.setPublisherTime(DateUtils.getNowDate());
        return sysArticleEntity;
    }

}
