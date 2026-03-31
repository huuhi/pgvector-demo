package com.huzhijian.pgvectordemo.service.impl;

import com.huzhijian.pgvectordemo.demo1.ChatAssistant;
import com.huzhijian.pgvectordemo.demo1.PgChatMemoryStore;
import com.huzhijian.pgvectordemo.domain.dto.ChatDTO;
import com.huzhijian.pgvectordemo.service.ChatService;
import com.huzhijian.pgvectordemo.service.McpSkillInformationService;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.skills.Skills;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/31
 * 说明:
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final McpSkillInformationService mcpSkillInformationService;
    private final PgChatMemoryStore chatMemoryStore;
    private final StreamingChatModel model;

    public ChatServiceImpl(McpSkillInformationService mcpSkillInformationService, PgChatMemoryStore chatMemoryStore, StreamingChatModel model) {
        this.mcpSkillInformationService = mcpSkillInformationService;
        this.chatMemoryStore = chatMemoryStore;
        this.model = model;
    }

    @Override
    public SseEmitter chat(ChatDTO chatDTO) {
        SseEmitter sseEmitter = new SseEmitter(120000L);
        AtomicBoolean isFinished = new AtomicBoolean();
        sseEmitter.onCompletion(()->isFinished.set(true));
        sseEmitter.onTimeout(()->isFinished.set(true));
        sseEmitter.onError((ex)->isFinished.set(true));
        String temp=chatDTO.getSessionId();
        String sessionId =(temp==null||temp.isEmpty())?UUID.randomUUID().toString():temp;


        Skills skills=mcpSkillInformationService.getSkills(chatDTO.getSkills());
        McpToolProvider mcpToolProvider=mcpSkillInformationService.getMcp(chatDTO.getMCPs());

        AiServices<ChatAssistant> chatAssistantAiServices = AiServices.builder(ChatAssistant.class)
                .streamingChatModel(model)
                .chatMemoryProvider(memoryId -> TokenWindowChatMemory.builder()
                        .chatMemoryStore(chatMemoryStore)
                        .maxTokens(8000, new OpenAiTokenCountEstimator("gpt-4o"))
                        .id(sessionId)
                        .build());
        if (skills!=null){
            chatAssistantAiServices.toolProvider(skills.toolProvider());
        }
        if (mcpToolProvider!=null){
            chatAssistantAiServices.toolProvider(mcpToolProvider);
        }



        ChatAssistant chatAssistant = chatAssistantAiServices.build();
                TokenStream tokenStream = chatAssistant.chat(chatDTO.getMsg(),sessionId);
        tokenStream.onPartialThinking(thinking -> {
            if (isFinished.get()) return;
            try {
                sseEmitter.send("[思考中]" + thinking.text());
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
        }).onPartialResponse(token -> {
            if (isFinished.get()) return;
            try {
                sseEmitter.send(token);
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
        }).onCompleteResponse(response -> {
            if (isFinished.get()) return;
            try {
                sseEmitter.send("\n\n[完毕]");
                sseEmitter.complete();
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
            log.info("思考内容:{},完整回复:{},Token消耗:{}",response.aiMessage().thinking(), response.aiMessage().text(), response.tokenUsage());
        }).onError(error -> {

        }).start();
        return sseEmitter;
    }
}
