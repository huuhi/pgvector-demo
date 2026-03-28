package com.huzhijian.pgvectordemo.service;

import com.huzhijian.pgvectordemo.domain.ChatHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

/**
* @author windows
* @description 针对表【chat_memory(存储会话记忆)】的数据库操作Service
* @createDate 2026-03-25 21:23:32
*/
public interface ChatMemoryService extends IService<ChatHistory> {
    List<ChatHistory> getByMemoryId(Object memory);
    void delByMemoryId(Object memoryId);
    void insertBatch(List<ChatHistory> list);

    List<ChatMessage> getHistoryBySessionId(String sessionId);
}
