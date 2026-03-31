package com.huzhijian.pgvectordemo.service;

import com.huzhijian.pgvectordemo.domain.dto.ChatDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/31
 * 说明:
 */
@Service
public interface ChatService {
    SseEmitter chat(ChatDTO chatDTO);
}
