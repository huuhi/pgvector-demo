package com.huzhijian.pgvectordemo.mapper;

import com.huzhijian.pgvectordemo.domain.ChatHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author windows
* @description 针对表【chat_memory(存储会话记忆)】的数据库操作Mapper
* @createDate 2026-03-25 21:23:32
* @Entity com.huzhijian.pgvectordemo.domain.ChatMemory
*/
@Mapper
public interface ChatMemoryMapper extends BaseMapper<ChatHistory> {
    List<ChatHistory> getAllByMemoryId(Object memoryId);

    void delAllByMemoryId(Object memoryId);

    boolean insertBatch(List<ChatHistory> list);
}




