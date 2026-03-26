package com.huzhijian.pgvectordemo.demo1;

import com.google.gson.Gson;
import com.huzhijian.pgvectordemo.domain.ChatMemory;
import com.huzhijian.pgvectordemo.service.ChatMemoryService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */

@Slf4j
@Service
public class PgChatMemoryStore implements ChatMemoryStore {
    @Resource
    private ChatMemoryService chatMemoryService;
    private final Gson gson=new Gson();

    @Override
    public List<ChatMessage> getMessages(Object o) {
        if (o==null||o=="") return List.of();
//        一般情况下memoryId都是字符串
        List<ChatMemory> chatMemories = chatMemoryService.getByMemoryId(o);
        if (chatMemories==null||chatMemories.isEmpty()) return List.of();
        return chatMemories.stream().map(entity->ChatMessageDeserializer
                .messageFromJson(entity.getContent())).toList();
    }

    @Override
    public void updateMessages(Object sessionId, List<ChatMessage> list) {
        if (sessionId==null||sessionId=="") throw new RuntimeException("会话ID不能为NULL/空");
        chatMemoryService.delByMemoryId(sessionId);
        ArrayList<ChatMemory> insertList = new ArrayList<>();
        for (ChatMessage chatMessage : list) {
            log.info("类型：{},chatMessage:{}",chatMessage.type(),chatMessage);
//
            String jsonString = ChatMessageSerializer.messageToJson(chatMessage);
            log.info("JSON:{}",jsonString);
            ChatMemory chatMemory = ChatMemory.builder()
                    .memoryId(sessionId)
                    .role(chatMessage.type().name())
                    .content(jsonString)
                    .build();
            insertList.add(chatMemory);
        }
        chatMemoryService.insertBatch(insertList);
    }

    @Override
    public void deleteMessages(Object o) {
        if (o==null||o=="") throw new RuntimeException("会话ID不能为NULL/空");
        chatMemoryService.delByMemoryId(o);
    }
}
