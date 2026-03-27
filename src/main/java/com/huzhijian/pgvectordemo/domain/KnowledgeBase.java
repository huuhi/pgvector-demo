package com.huzhijian.pgvectordemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 知识库表,存储知识库信息
 * @TableName knowledge_base
 */
@TableName(value ="knowledge_base")
@Data
public class KnowledgeBase {
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
     * 
     */
    private String tableName;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Boolean isPrivate;
}