package com.huzhijian.pgvectordemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.UUID;

import com.huzhijian.pgvectordemo.config.UuidTypeHandler;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

/**
 * 存储会话记忆
 * @TableName chat_memory
 */
@TableName(value ="chat_memory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemory {
    /**
     * 会话ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 角色
     */
    private Object role;
//    @TableField(value = "memory_id",jdbcType = JdbcType.OTHER,typeHandler = UuidTypeHandler.class)
    private Object memoryId;


    /**
     * 创建时间
     */
    private Date createAt;
}