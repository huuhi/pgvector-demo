package com.huzhijian.pgvectordemo.apiTest;

import org.junit.jupiter.api.Test;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/30
 * 说明:
 */
public class RegexTest {
    @Test
    void testRegexDemo1(){
        String a="npx skills list";
        String[] blackList={";","&","|",">","<","$","`","\n"};
        for (String s : blackList) {
            if (a.contains(s)){
                System.out.println("包含非法字符");
            }
        }
        System.out.println("合法");
    }
}
