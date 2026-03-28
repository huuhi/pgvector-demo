package com.huzhijian.pgvectordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huzhijian.pgvectordemo.domain.ChatHistory;
import com.huzhijian.pgvectordemo.service.ChatMemoryService;
import com.huzhijian.pgvectordemo.mapper.ChatMemoryMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author windows
* @description 针对表【chat_memory(存储会话记忆)】的数据库操作Service实现
* @createDate 2026-03-25 21:23:32
*/
@Service
public class ChatMemoryServiceImpl extends ServiceImpl<ChatMemoryMapper, ChatHistory>
    implements ChatMemoryService{

    @Resource
    private ChatMemoryMapper mapper;

    @Override
    public List<ChatHistory> getByMemoryId(Object memory) {
        return mapper.getAllByMemoryId(memory);
    }

    @Override
    public void delByMemoryId(Object memoryId) {
        mapper.delAllByMemoryId(memoryId);
    }

    @Override
    public void insertBatch(List<ChatHistory> list) {
        mapper.insertBatch(list);
    }
//dev.langchain4j.data.message;
//dev.langchain4j.data.message.ChatMessage
    @Override
    public List<ChatMessage> getHistoryBySessionId(String sessionId) {
        List<ChatHistory> memoryList = mapper.getAllByMemoryId(sessionId);
        if (memoryList==null||memoryList.isEmpty()) return List.of();
        return memoryList.stream().map(m-> ChatMessageDeserializer.messageFromJson(m.getContent())).toList();
    }
}




