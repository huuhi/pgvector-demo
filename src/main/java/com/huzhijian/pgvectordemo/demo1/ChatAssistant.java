package com.huzhijian.pgvectordemo.demo1;

import dev.langchain4j.service.*;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
public interface ChatAssistant {
    @SystemMessage("当前会话ID为:{{sessionId}}，适当的加载skill学习以及合理运用提供的工具")
    TokenStream chat(@UserMessage String msg,   @V("sessionId") @MemoryId Object memoryId);
}
