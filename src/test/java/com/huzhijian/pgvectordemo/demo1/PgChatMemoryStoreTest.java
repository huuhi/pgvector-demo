package com.huzhijian.pgvectordemo.demo1;

import dev.langchain4j.data.message.ChatMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/28
 * 说明:
 */
@Slf4j
@SpringBootTest
class PgChatMemoryStoreTest {
    @Resource
    private PgChatMemoryStore pgChatMemoryStore;

    @Test
    void getMessages() {
        List<ChatMessage> messages = pgChatMemoryStore.getMessages("aa560343-9c78-4e43-9559-2935c315348e");
        for (ChatMessage message : messages) {
            log.info("message:{}",message);
        }
    }

    @Test
    void updateMessages() {
    }

    @Test
    void deleteMessages() {
    }
}