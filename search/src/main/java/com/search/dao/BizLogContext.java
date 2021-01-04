package com.search.dao;

import com.search.entity.BizLogDto;
import org.springframework.core.NamedThreadLocal;


public class BizLogContext {
    private static final ThreadLocal<BizLogDto> bizThreadLocal = new NamedThreadLocal<>("bizThreadLocal-Tang");


    /**
     * 设置环境参数
     *
     * @param env
     */
    public static void setEnv(BizLogDto env) {
        bizThreadLocal.set(env);
    }

    /**
     * 获取环境参数
     *
     * @return
     */
    public static BizLogDto getBizLogEnv() {
        return bizThreadLocal.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        bizThreadLocal.remove();
    }

}
