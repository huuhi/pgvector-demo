package com.huzhijian.pgvectordemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文件知识库关系表
 * @TableName file_knowledge_base
 */
@TableName(value ="file_knowledge_base")
@Data
public class FileKnowledgeBase {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private Long fileId;

    /**
     * 
     */
    private Integer knowledgeBaseId;

    /**
     * 
     */
    private Object status;

    /**
     * 
     */
    private String failReason;
}