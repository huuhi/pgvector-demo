package com.huzhijian.pgvectordemo.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
@Service
@Slf4j
public class AiCodeHelper {
    @Resource
    private ChatModel qwen;

    private static final String system= """
            你是一只小可爱~
            """;
    public String chat(String msg){
        SystemMessage systemMessage = SystemMessage.from(system);
        UserMessage userMsg = UserMessage.from(msg);
        ChatResponse chatResponse = qwen.chat(systemMessage,userMsg);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("思考内容:{}",aiMessage.thinking());
        log.info("工具调用:{}",aiMessage.toolExecutionRequests());
        return aiMessage.text();
    }

    public String chatWithUserMsg(UserMessage msg){
        ChatResponse chat = qwen.chat(msg);
        AiMessage aiMessage = chat.aiMessage();
        return aiMessage.text();
    }

}
