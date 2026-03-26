package com.huzhijian.pgvectordemo.demo1;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
public interface ChatAssistant {
    TokenStream chat(@UserMessage dev.langchain4j.data.message.UserMessage msg, @MemoryId Object memoryId);
}
