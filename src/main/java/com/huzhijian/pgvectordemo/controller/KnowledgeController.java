package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.service.KnowledgeService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/26
 * 说明:
 */
@RestController("/knowledge")
public class KnowledgeController {
    @Resource
    private KnowledgeService knowledgeService;

    @PostMapping("/text")
    public ResponseEntity<String> insertText(@RequestParam String text,@RequestParam String table){
        boolean success=knowledgeService.insertText(text,table);
        return success?ResponseEntity.ok("插入成功"):ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("插入失败");
    }
//    上传文件,支持上传多个文件，不搞OSS了，直接上传到本地即可
    @PostMapping("/files")
    public ResponseEntity<String> uploadFiles(@RequestParam MultipartFile[] files){
        knowledgeService.insertFiles(files);
        return ResponseEntity.ok("上传成功");
    }
//    将文件添加到知识库中

    /**
     *
     * @param table 表名称
     * @param ids  文件ID
     * @return 返回是否成功
     */
    @PostMapping("/embedding")
    public ResponseEntity<String> addFile(@RequestParam String table,@RequestParam List<Long> ids){
        knowledgeService.insertKnowledgeFile(table,ids);
        return ResponseEntity.ok("正在向量化");
    }
}
