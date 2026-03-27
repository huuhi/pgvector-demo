package com.huzhijian.pgvectordemo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.huzhijian.pgvectordemo.em.Status;
import lombok.Builder;
import lombok.Data;

/**
 * 文件
 * @TableName files
 */
@TableName(value ="files")
@Data
@Builder
public class KnowledgeFiles {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 
     */
    private Long fileSize;

    /**
     * 文件原始名称
     */
    private String fileName;

    /**
     * 
     */
    private String failReason;

    /**
     * 
     */
    private Status status;

    /**
     * 
     */
    private Date createTime;
}