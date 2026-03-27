package com.huzhijian.pgvectordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huzhijian.pgvectordemo.domain.KnowledgeBase;
import com.huzhijian.pgvectordemo.service.KnowledgeBaseService;
import com.huzhijian.pgvectordemo.mapper.KnowledgeBaseMapper;
import org.springframework.stereotype.Service;

/**
* @author windows
* @description 针对表【knowledge_base(知识库表,存储知识库信息)】的数据库操作Service实现
* @createDate 2026-03-27 12:19:16
*/
@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase>
    implements KnowledgeBaseService{

}




