package com.huzhijian.pgvectordemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * 命令行授权
 * @TableName directive_auth
 */
@TableName(value ="directive_auth")
@Data
@Builder
public class DirectiveAuth {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private String directive;

    /**
     * 
     */
    private String memoryId;
}