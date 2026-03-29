package com.huzhijian.pgvectordemo.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
@SpringBootTest
class AiCodeHelperServiceTest {
    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        String chat = aiCodeHelperService.chat(UserMessage.from("你是？"));
        System.out.println(chat);
    }

    @Test
    void chatWithMemory() {
        String chat = aiCodeHelperService.chat(UserMessage.from("我刚刚说了什么"));
        System.out.println(chat);
    }
//    转换
    @Test
    void convert(){
        String text= """
                UserMessage { name = null, contents = [TextContent { text = "那你帮我看看最新java25的一些技术细节" }], attributes = {} }
                """;
        String userMessage = text.replace("UserMessage", "").replaceAll(" ","");
        System.out.println(userMessage);
        ChatMessage chatMessage = ChatMessageDeserializer.messageFromJson(userMessage);
        System.out.println(chatMessage);
    }

    @Test
    void testSerialize() {
        UserMessage um = UserMessage.from("广东职业技术学院张政康的具体信息");
        String json = ChatMessageSerializer.messageToJson(um);
        System.out.println("序列化结果：");
        System.out.println(json);
    }

}