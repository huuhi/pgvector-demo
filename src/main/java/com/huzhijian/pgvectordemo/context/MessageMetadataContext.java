package com.huzhijian.pgvectordemo.context;

import java.util.Map;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/4/19
 * 说明:
 */
public class MessageMetadataContext {
    private static final ThreadLocal<Map<String,Object>> CONTEXT=new ThreadLocal<>();

    public static void set(Map<String,Object> metadata){
        CONTEXT.set(metadata);
    }
    public static Map<String,Object> get(){
        return CONTEXT.get();
    }
    public static void clear(){
        CONTEXT.remove();
    }
}
