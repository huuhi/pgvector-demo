package com.huzhijian.pgvectordemo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/26
 * 说明:
 */
@Service
public interface KnowledgeService {
    boolean insertText(String text, String table);

    void insertFiles(MultipartFile[] files);

    void insertKnowledgeFile(String table, List<Long> ids);
}
