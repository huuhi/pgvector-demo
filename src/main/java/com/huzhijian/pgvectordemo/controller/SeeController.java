package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.demo1.ChatAssistant;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

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
    private ChatAssistant chatAssistant;

    @GetMapping("/stream")
    public SseEmitter streamChat(@RequestParam String message,@RequestParam Object memoryId) {
        SseEmitter sseEmitter = new SseEmitter(120000L);
        if (memoryId==null||memoryId==""){
            memoryId= UUID.randomUUID().toString();
        }
        TokenStream tokenStream = chatAssistant.chat(message,memoryId);
        tokenStream.onPartialThinking(thinking -> {
            try {
                sseEmitter.send("[思考中]" + thinking.text());
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
        }).onPartialResponse(token -> {
            try {
                sseEmitter.send(token);
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
        }).onCompleteResponse(response -> {
            try {
                sseEmitter.send("\n\n[完毕]");
                sseEmitter.complete();
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
            log.info("思考内容:{},完整回复:{},Token消耗:{}",response.aiMessage().thinking(), response.aiMessage().text(), response.tokenUsage());
        }).onError(error -> {
            sseEmitter.completeWithError(error);
            error.printStackTrace();
        }).start();
        return sseEmitter;
    }

}
