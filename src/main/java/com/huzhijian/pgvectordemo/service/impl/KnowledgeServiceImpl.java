package com.huzhijian.pgvectordemo.service.impl;

import com.huzhijian.pgvectordemo.domain.entity.KnowledgeFiles;
import com.huzhijian.pgvectordemo.service.FileKnowledgeBaseService;
import com.huzhijian.pgvectordemo.service.KnowledgeService;
import com.huzhijian.pgvectordemo.utils.FileUtils;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/26
 * 说明:
 */
@Slf4j
@Service
public class KnowledgeServiceImpl implements KnowledgeService {
    private final Function<String, PgVectorEmbeddingStore> factory;
    private final EmbeddingModel embeddingModel;
    private final FileUtils fileUtils;
    private final FileKnowledgeBaseService fileKnowledgeBaseService;

    public KnowledgeServiceImpl(Function<String, PgVectorEmbeddingStore> factory, EmbeddingModel embeddingModel, FileUtils fileUtils, FileKnowledgeBaseService fileKnowledgeBaseService) {
        this.factory = factory;
        this.embeddingModel = embeddingModel;
        this.fileUtils = fileUtils;
        this.fileKnowledgeBaseService = fileKnowledgeBaseService;
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
        return id!=null;
    }

    @Override
    public void insertFiles(MultipartFile[] files) {
        fileUtils.uploadFils(files);
    }
    @Override
    public void insertKnowledgeFile(String table, List<Long> ids) {
        List<KnowledgeFiles> files = fileUtils.getFilesById(ids);
        PgVectorEmbeddingStore store = factory.apply(table);
        fileKnowledgeBaseService.paresFileAndAddKnowledgeBase(files,store);
    }
}
