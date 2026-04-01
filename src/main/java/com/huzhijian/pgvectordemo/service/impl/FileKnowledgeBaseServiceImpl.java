package com.huzhijian.pgvectordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huzhijian.pgvectordemo.domain.entity.FileKnowledgeBase;
import com.huzhijian.pgvectordemo.domain.entity.KnowledgeFiles;
import com.huzhijian.pgvectordemo.service.FileKnowledgeBaseService;
import com.huzhijian.pgvectordemo.mapper.FileKnowledgeBaseMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author windows
* @description 针对表【file_knowledge_base(文件知识库关系表)】的数据库操作Service实现
* @createDate 2026-03-27 12:19:24
*/
@Service
@Slf4j
public class FileKnowledgeBaseServiceImpl extends ServiceImpl<FileKnowledgeBaseMapper, FileKnowledgeBase>
    implements FileKnowledgeBaseService{
    private final EmbeddingModel embeddingModel;
    private final FileKnowledgeBaseMapper mapper;

    public FileKnowledgeBaseServiceImpl(EmbeddingModel embeddingModel, FileKnowledgeBaseMapper mapper) {
        this.embeddingModel = embeddingModel;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void addFile(List<KnowledgeFiles> files) {
//        这里的知识库ID直接为1，不分表了，具体实现新开一个项目写
        List<FileKnowledgeBase> list = new ArrayList<>();
        for (KnowledgeFiles file : files) {
            FileKnowledgeBase fileKnowledgeBase = FileKnowledgeBase.builder()
                    .knowledgeBaseId(1)
                    .fileId(file.getId())
                    .status("PROCESSING")
                    .build();
            list.add(fileKnowledgeBase);
        }
        saveBatch(list);
    }

    @Override
    @Async
    public void paresFileAndAddKnowledgeBase(List<KnowledgeFiles> files,PgVectorEmbeddingStore store) {
//   知识库还是不分表吧，我寻思压根没人用户啊，到时候还是直接加用户ID得了
        List<FileKnowledgeBase> list = new ArrayList<>();
        //        每隔500个字符切分一下，重叠50字符
        DocumentSplitter splitter = DocumentSplitters.recursive(850, 150);
        for (KnowledgeFiles file : files) {
            Document document = FileSystemDocumentLoader.loadDocument(file.getFilePath(), new ApacheTikaDocumentParser());
            document.metadata().put("fileId", file.getId()).put("original_file_name", file.getFileName());
            List<TextSegment> segments = splitter.split(document);
            log.info("块大小:{}", segments.size());
//                分批添加
//            SUCCESS
            FileKnowledgeBase fileKnowledgeBase = FileKnowledgeBase.builder().status("SUCCESS").fileId(file.getId()).build();


            List<List<TextSegment>> batches = ListUtils.partition(segments, 10);
            for (List<TextSegment> batch : batches) {
                try {
                    List<Embedding> content = embeddingModel.embedAll(batch).content();
                    store.addAll(content, segments);
                    Thread.sleep(200);
                } catch (Exception e) {
                    // TODO 处理异常
                    fileKnowledgeBase.setStatus("FAIL");
                    fileKnowledgeBase.setFailReason(e.getMessage().substring(185));
                    log.error("休眠失败");
                }
            }
            list.add(fileKnowledgeBase);
        }
//        TODO 这里的实现依托，fileId是可能重复的，算了算了，毕竟是只是学习demo
        list.forEach(f->{
            lambdaUpdate().eq(FileKnowledgeBase::getFileId,f.getFileId())
                    .update();
        });
    }
}




