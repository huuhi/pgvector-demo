package com.huzhijian.pgvectordemo.em;

import lombok.Getter;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/27
 * 说明:
 */
public enum Status {
//    'FAIL','UPLOADING','PROCESSING','SUCCESS'
    FAIL("FAIL"),UPLOADING("UPLOADING"),PROCESSING("PROCESSING"),SUCCESS("SUCCESS");
    @Getter
    private final String value;
    Status(String value){
        this.value=value;
    }
}
