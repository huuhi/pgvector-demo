package com.huzhijian.pgvectordemo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/31
 * 说明:
 */
@Builder
@Data
@AllArgsConstructor
public class McpSkillVO {
    private String name;


    /**
     * MCP的类型,HTTP或SSE
     */
    private String mcpType;

    /**
     *
     */
    private String sourcePath;
}
