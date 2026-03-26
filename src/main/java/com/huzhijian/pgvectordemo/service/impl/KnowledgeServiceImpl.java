package com.huzhijian.pgvectordemo.service.impl;

import com.huzhijian.pgvectordemo.demo1.PgVectorEmbeddingFactory;
import com.huzhijian.pgvectordemo.service.KnowledgeService;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.util.Map;
import java.util.function.Function;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/26
 * 说明:
 */
public class KnowledgeServiceImpl implements KnowledgeService {
    private final Function<String, PgVectorEmbeddingStore> factory;
    private final EmbeddingModel embeddingModel;
    public KnowledgeServiceImpl(Function<String, PgVectorEmbeddingStore> factory, EmbeddingModel embeddingModel) {
        this.factory = factory;
        this.embeddingModel = embeddingModel;
    }

    @Override
    public boolean insertText(String text, String table) {
        PgVectorEmbeddingStore store = factory.apply(table);
        Response<Embedding> embed = embeddingModel.embed(text);
//        元数据
        Map<String, Object> metadataMap = embed.metadata();
        Metadata metadata = Metadata.from(metadataMap);
        TextSegment textSegment = TextSegment.from(text, metadata);
        String id = store.add(embed.content(), textSegment);
        return id==null;
    }
}
