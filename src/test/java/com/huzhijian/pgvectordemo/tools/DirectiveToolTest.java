package com.huzhijian.pgvectordemo.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/31
 * 说明:
 */
@SpringBootTest
public class DirectiveToolTest {
    @Resource
    private ExecutionDirectiveTool executionDirectiveTool;

    @Test
    void testDir(){
        System.out.println(executionDirectiveTool.executionDirective("dir  E:\\jcode\\demo\\pgvector-demo", "2123fa11-0a23-4c82-9ba4-711051e051de"));
    }
}
