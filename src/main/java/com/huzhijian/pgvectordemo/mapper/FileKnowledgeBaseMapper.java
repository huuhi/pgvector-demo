package com.huzhijian.pgvectordemo.mapper;

import com.huzhijian.pgvectordemo.domain.entity.FileKnowledgeBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author windows
* @description 针对表【file_knowledge_base(文件知识库关系表)】的数据库操作Mapper
* @createDate 2026-03-27 12:19:24
* @Entity com.huzhijian.pgvectordemo.domain.entity.FileKnowledgeBase
*/
public interface FileKnowledgeBaseMapper extends BaseMapper<FileKnowledgeBase> {

    void updateBatchById(List<FileKnowledgeBase> list);
}




