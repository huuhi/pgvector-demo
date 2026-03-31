package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.demo1.ChatAssistant;
import com.huzhijian.pgvectordemo.domain.dto.ChatDTO;
import com.huzhijian.pgvectordemo.service.ChatService;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
@RestController
@Slf4j
public class SeeController {

    @Resource
    private ChatService chatService;

    @PostMapping("/stream")
    public SseEmitter streamChat(@RequestBody ChatDTO chatDTO) {
        return chatService.chat(chatDTO);
    }

}
