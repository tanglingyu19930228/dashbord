package com.search.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.function.ToLongFunction;

import com.search.common.utils.BigDecimalUtils;
import com.search.entity.StatisticsResp;
import com.search.entity.SumVoiceResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.search.dao.SysArticleDao;
import com.search.entity.SysArticleEntity;
import com.search.service.ISysArticleService;
import com.search.common.text.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * SysArticleService业务层处理
 *
 * @date 2020-12-09 23:40:13
 */
@Service
@Slf4j
public class SysArticleServiceImpl implements ISysArticleService {
    @Autowired
    private SysArticleDao sysArticleDao;

    /**
     * 查询SysArticleEntity
     *
     * @param id id
     * @return SysArticleEntity
     */
    @Override
    public SysArticleEntity selectSysArticleById(Integer id) {
        SysArticleEntity sysArticleEntity = new SysArticleEntity();
        sysArticleEntity.setId(id);
        return this.selectSysArticleOne(sysArticleEntity);
    }

    /**
     * 查询SysArticleEntity
     *
     * @param sysArticleEntity sysArticleEntity
     * @return SysArticleEntity
     */
    @Override
    public SysArticleEntity selectSysArticleOne(SysArticleEntity sysArticleEntity) {
        try {
            return sysArticleDao.selectSysArticleOne(sysArticleEntity);
        } catch (Exception e) {
            log.error("数据库异常:", e);
            return null;
        }
    }

    /**
     * 查询SysArticle列表
     *
     * @param sysArticleEntity sysArticleEntity
     * @return SysArticleEntityList 列表
     */
    @Override
    public List<SysArticleEntity> selectSysArticleList(SysArticleEntity sysArticleEntity) {
        try {
            return sysArticleDao.selectSysArticleList(sysArticleEntity);
        } catch (Exception e) {
            log.error("数据库异常:", e);
            return new ArrayList<SysArticleEntity>();
        }
    }

    /**
     * 新增SysArticleEntity
     *
     * @param sysArticleEntity sysArticleEntity
     * @return 结果
     */
    @Override
    public int insertSysArticle(SysArticleEntity sysArticleEntity) {
        List<SysArticleEntity> list = new ArrayList<>();
        list.add(sysArticleEntity);
        return this.insertSysArticleList(list);
    }

    /**
     * 新增sysArticleEntity
     *
     * @param list list
     * @return 结果
     */
    @Override
    public int insertSysArticleList(List<SysArticleEntity> list) {
        try {
            return sysArticleDao.insertSysArticle(list);
        } catch (Exception e) {
            log.error("数据库异常:", e);
            return 0;
        }
    }

    /**
     * 修改SysArticleEntity
     *
     * @param sysArticleEntity SysArticleEntity
     * @return 结果
     */
    @Override
    public int updateSysArticle(SysArticleEntity sysArticleEntity) {
        List<SysArticleEntity> list = new ArrayList<>();
        list.add(sysArticleEntity);
        return this.updateSysArticle(list);
    }

    /**
     * 修改SysArticleEntity
     *
     * @param sysArticleEntityList sysArticleEntityList
     * @return 结果
     */
    @Override
    public int updateSysArticle(List<SysArticleEntity> sysArticleEntityList) {
        try {
            return sysArticleDao.updateSysArticle(sysArticleEntityList);
        } catch (Exception e) {
            log.error("数据库异常:", e);
            return 0;
        }
    }

    @Override
    public List<SumVoiceResp> sumVoiceTrendcy() {
        return sysArticleDao.sumVoiceTrendcy();
    }

    @Override
    public String avgVoiceTrendcy() {
        List<SumVoiceResp> sumVoiceResps = sysArticleDao.sumVoiceTrendcy();
        double asDouble = sumVoiceResps.stream().mapToLong(new ToLongFunction<SumVoiceResp>() {
            @Override
            public long applyAsLong(SumVoiceResp value) {
                return value.getTotal();
            }
        }).average().getAsDouble();
        return BigDecimalUtils.round(asDouble,2).toString();
    }

    @Override
    public List<StatisticsResp> statisticsVoice() {
        return sysArticleDao.statisticsVoice();
    }

    @Override
    public Long totalVoice() {
        return sysArticleDao.totalVoice();
    }

    @Override
    public List<StatisticsResp> sysLike(SysArticleEntity sysArticleEntity) {
        return sysArticleDao.sysLike(sysArticleEntity);
    }
}
