package com.huzhijian.pgvectordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huzhijian.pgvectordemo.domain.entity.KnowledgeFiles;
import com.huzhijian.pgvectordemo.service.FilesService;
import com.huzhijian.pgvectordemo.mapper.FilesMapper;
import org.springframework.stereotype.Service;

/**
* @author windows
* @description 针对表【files(文件)】的数据库操作Service实现
* @createDate 2026-03-27 12:18:52
*/
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, KnowledgeFiles>
    implements FilesService{

}




