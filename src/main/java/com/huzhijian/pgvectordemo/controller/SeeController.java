package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.domain.dto.ChatDTO;
import com.huzhijian.pgvectordemo.service.ChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

    @PostMapping("/image")
    public SseEmitter streamChatWithImage(@RequestBody ChatDTO chatDTO) {
        return chatService.chatWithChatRequest(chatDTO);
    }

}
