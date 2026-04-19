package com.huzhijian.pgvectordemo.service.impl;

import com.huzhijian.pgvectordemo.context.MessageMetadataContext;
import com.huzhijian.pgvectordemo.demo1.ChatAssistant;
import com.huzhijian.pgvectordemo.demo1.PgChatMemoryStore;
import com.huzhijian.pgvectordemo.domain.dto.ChatDTO;
import com.huzhijian.pgvectordemo.service.ChatService;
import com.huzhijian.pgvectordemo.service.McpSkillInformationService;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.skills.Skills;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .chatMemoryStore(chatMemoryStore)
                        .maxMessages(20)
//                        .maxTokens(8000, new OpenAiTokenCountEstimator("gpt-4o"))
                        .id(sessionId)
                        .build());
        if (skills!=null){
            chatAssistantAiServices.toolProvider(skills.toolProvider());
        }
        if (mcpToolProvider!=null){
            chatAssistantAiServices.toolProvider(mcpToolProvider);
        }

        ChatAssistant chatAssistant = chatAssistantAiServices.build();
        String imageUrl = chatDTO.getImageUrl();
        ImageContent imageContent = ImageContent.from(imageUrl);
        TextContent textContent = TextContent.from(chatDTO.getMsg());
        TokenStream tokenStream = chatAssistant.chat(List.of(imageContent,textContent),sessionId);
        return getSseEmitter(sseEmitter, isFinished, tokenStream);
    }

    /**
     * 直接发送ChatRequest,底层执行toString
     * @param chatDTO 参数
     * @return Sse流
     * 这个接口是不行滴！
     */
    @Override
    public SseEmitter chatWithChatRequest(ChatDTO chatDTO) {
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
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .chatMemoryStore(chatMemoryStore)
                        .maxMessages(20)
//                        .maxTokens(8000, new OpenAiTokenCountEstimator("gpt-4o"))
                        .id(sessionId)
                        .build());
        if (skills!=null){
            chatAssistantAiServices.toolProvider(skills.toolProvider());
        }
        if (mcpToolProvider!=null){
            chatAssistantAiServices.toolProvider(mcpToolProvider);
        }
//        这里是一个简单的实现，实际项目中需要文件名称，大小，ID等实际数据
        String imageUrl = chatDTO.getImageUrl();
        ImageContent imageContent = ImageContent.from(imageUrl);
        TextContent textContent = TextContent.from(chatDTO.getMsg());
        MessageMetadataContext.set(Map.of("image_url",imageUrl));

        ChatAssistant chatAssistant = chatAssistantAiServices.build();
        TokenStream tokenStream = chatAssistant.chat(List.of(imageContent, textContent),sessionId);
        return getSseEmitter(sseEmitter, isFinished, tokenStream);
    }

    private SseEmitter getSseEmitter(SseEmitter sseEmitter, AtomicBoolean isFinished, TokenStream tokenStream) {
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
