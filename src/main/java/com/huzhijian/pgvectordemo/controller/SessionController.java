package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.demo1.PgChatMemoryStore;
import com.huzhijian.pgvectordemo.domain.dto.MessageDTO;
import com.huzhijian.pgvectordemo.service.ChatMemoryService;
import dev.langchain4j.data.message.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/28
 * 说明: 会话控制器
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    private final ChatMemoryService chatMemoryService;
    private final PgChatMemoryStore pgChatMemoryStore;

    public SessionController(ChatMemoryService chatMemoryService, PgChatMemoryStore pgChatMemoryStore) {
        this.chatMemoryService = chatMemoryService;
        this.pgChatMemoryStore = pgChatMemoryStore;
    }


    @GetMapping("/{sessionId}/history")
    public ResponseEntity<List<MessageDTO>> getHistoryBySessionId(@PathVariable String sessionId){
        List<ChatMessage> history=pgChatMemoryStore.getMessages(sessionId);
        List<MessageDTO> histories=chatMemoryService.getHistory(history);
        return ResponseEntity.ok(histories);
    }
    @DeleteMapping("/{sessionId}/history")
    public ResponseEntity<String> DelHistoryBySessionId(@PathVariable String sessionId){
        chatMemoryService.delByMemoryId(sessionId);
        return ResponseEntity.ok("删除成功");
    }
}
