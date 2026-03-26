package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.service.KnowledgeService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/26
 * 说明:
 */
@RestController
public class KnowledgeController {
    @Resource
    private KnowledgeService knowledgeService;

    @PostMapping("/insert-text")
    public ResponseEntity<?> insertText(@RequestParam String text,@RequestParam String table){
        boolean success=knowledgeService.insertText(text,table);
        return success?ResponseEntity.ok("插入成功"):ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
