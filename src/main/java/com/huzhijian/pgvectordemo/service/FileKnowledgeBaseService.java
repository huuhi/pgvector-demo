package com.huzhijian.pgvectordemo.service;

import com.huzhijian.pgvectordemo.domain.entity.FileKnowledgeBase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huzhijian.pgvectordemo.domain.entity.KnowledgeFiles;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
* @author windows
* @description 针对表【file_knowledge_base(文件知识库关系表)】的数据库操作Service
* @createDate 2026-03-27 12:19:24
*/
public interface FileKnowledgeBaseService extends IService<FileKnowledgeBase> {
//    添加文件到知识库
    void addFile(List<KnowledgeFiles> files);

    @Async
    void paresFileAndAddKnowledgeBase(List<KnowledgeFiles> files, PgVectorEmbeddingStore store);
}
