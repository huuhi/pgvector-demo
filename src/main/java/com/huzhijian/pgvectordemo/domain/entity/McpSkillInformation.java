package com.huzhijian.pgvectordemo.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 存储MCP和SKILL的信息
 * @TableName mcp_skill_information
 */
@TableName(value ="mcp_skill_information")
@Data
@AllArgsConstructor
public class McpSkillInformation {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 是否为mcp，如果不是则是skill
     */
    private Boolean isMcp;

    /**
     * MCP的类型,HTTP或SSE
     */
    private String mcpType;

    /**
     * 
     */
    private String sourcePath;
}