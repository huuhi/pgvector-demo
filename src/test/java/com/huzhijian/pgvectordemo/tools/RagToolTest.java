package com.huzhijian.pgvectordemo.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/4/1
 * 说明:
 */
@SpringBootTest
public class RagToolTest {
    @Resource
    private RagTool tool;

    @Test
    void testQuery(){
        System.out.println(tool.ragSearch("校园寻踪：数字奇兵大冒险"));
    }
}
