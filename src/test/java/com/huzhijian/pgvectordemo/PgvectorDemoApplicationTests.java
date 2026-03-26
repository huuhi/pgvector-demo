package com.huzhijian.pgvectordemo;

import com.huzhijian.pgvectordemo.ai.AiCodeHelper;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PgvectorDemoApplicationTests {

    @Resource
    private AiCodeHelper aiCodeHelper;

    @Test
    void contextLoads() {
        String chat = aiCodeHelper.chat("你可以使用联网搜索吗？");
        System.out.println(chat);
    }

    @Test
    void testUserMsg(){
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://avatars.githubusercontent.com/u/167986026?v=4&size=64")
        );
        System.out.println(aiCodeHelper.chatWithUserMsg(userMessage));
    }
}
