package com.search.sync;

import com.search.bean.ElasticsearchConfig;
import com.search.common.utils.GuavaCacheUtils;
import com.search.common.utils.R;
import com.search.common.utils.StringUtils;
import com.search.dao.SysArticleDao;
import com.search.entity.SysArticleEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class CombineDatabaseAndElasticsearch {

    private static final String  SYNC_KEY = "sync";

    @Autowired
    SysArticleDao sysArticleDao;

    @Autowired
    ElasticsearchUtils elasticsearchUtils;

    public Integer getDatabaseSyncStart(){
        log.info("select elasticsearch last id~~~~~~~~~~~~~~");
        try {
            String id = elasticsearchUtils.getElasticIndexLastInsertId("newindex");
            if(StringUtils.isBlank(id)){
                return 0;
            }
            return Integer.parseInt(id);
        } catch (Exception e) {
            log.error("select last elasticsearch start error",e);
            return 0;
        }
    }

    public List<SysArticleEntity> getNeedSyncList(){
        log.info("开始查询待同步数据");
        try {
            Integer databaseSyncStart = getDatabaseSyncStart();
//            if(databaseSyncStart ==0){
//                return new ArrayList<>();
//            }
            return sysArticleDao.selectListByNeedSync(databaseSyncStart);
        } catch (Exception e) {
            log.info("服务器异常",e);
            return new ArrayList<>();
        }
    }

    /**
     * 同步调用
     * @return 做同步
     */
    @Scheduled(cron = "* * * * * 1")
    public synchronized R doSync(){
        log.info("开始同步数据库和es中的数据");
        try {
//            final Object o = GuavaCacheUtils.cache.get(SYNC_KEY);
//            if(Objects.nonNull(o)){
//                log.info("有同步任务正在执行");
//                return R.error("有同步任务正在执行");
//            }
            List<SysArticleEntity> needSyncList = getNeedSyncList();
            int result = elasticsearchUtils.syncDatabaseToElasticsearchBulk("newindex6", needSyncList);
            if(result==needSyncList.size()){
                return R.ok("同步完毕");
            }
            String data = "同步成功了："+result+";总需同步数量为："+needSyncList.size();
            return R.ok(data);
        } catch (Exception e) {
            log.error("服务器出错",e);
            return  R.error();
        }
    }
}
