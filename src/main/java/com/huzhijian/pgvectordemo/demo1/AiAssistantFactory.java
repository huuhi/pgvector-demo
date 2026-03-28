package com.huzhijian.pgvectordemo.demo1;

import com.huzhijian.pgvectordemo.tools.GetWeather;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.skills.FileSystemSkill;
import dev.langchain4j.skills.FileSystemSkillLoader;
import dev.langchain4j.skills.Skills;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.function.Function;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
@Component
public class AiAssistantFactory {
    @Resource
    private StreamingChatModel streamingChatModel;
    @Resource
    private PgChatMemoryStore chatMemoryStore;

    @Resource
    private McpToolProvider provider;
    @Resource
    private  GetWeather getWeather;


//  检索器
    @Bean
    public ContentRetriever contentRetriever(EmbeddingModel model, Function<String, PgVectorEmbeddingStore> factory){
        PgVectorEmbeddingStore store = factory.apply("test");

        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(5)
                .minScore(0.7)
                .build();
    }


    @Bean
    public ChatAssistant chatAssistant(ContentRetriever contentRetriever){
        Path path = Path.of(System.getProperty("user.home")+"/.agents/skills");
        Skills skills = Skills.from(FileSystemSkillLoader.loadSkills(path));
        return AiServices.builder(ChatAssistant.class)
                .streamingChatModel(streamingChatModel)
                .contentRetriever(contentRetriever)
                .tools(getWeather)
                .toolProviders(provider,skills.toolProvider())
                .chatMemoryProvider(
                        memoryId-> MessageWindowChatMemory.builder()
                        .chatMemoryStore(chatMemoryStore)
                        .id(memoryId).maxMessages(20).build())
                .build();
    }
}
