package com.huzhijian.pgvectordemo.demo1;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
@Component
public class AiAssistantFactory {
    @Resource
    private StreamingChatModel streamingChatModel;
    @Resource
    private PgChatMemoryStore chatMemoryStore;


    @Bean
    public ChatAssistant chatAssistant(){


        return AiServices.builder(ChatAssistant.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(
                        memoryId-> MessageWindowChatMemory.builder()
                        .chatMemoryStore(chatMemoryStore)
                        .id(memoryId).maxMessages(20).build())
                .build();
    }
}
