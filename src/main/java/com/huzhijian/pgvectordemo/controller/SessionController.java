package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.domain.vo.MessageVO;
import com.huzhijian.pgvectordemo.service.ChatMemoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/28
 * 说明: 会话控制器
 */
@RestController
@RequestMapping("/session")
@CrossOrigin(origins = "*")
public class SessionController {
    private final ChatMemoryService chatMemoryService;

    public SessionController(ChatMemoryService chatMemoryService) {
        this.chatMemoryService = chatMemoryService;
    }


    @GetMapping("/{sessionId}/history")
    public ResponseEntity<List<MessageVO>> getHistoryBySessionId(@PathVariable String sessionId){
        List<MessageVO> histories=chatMemoryService.getHistory(sessionId);
        return ResponseEntity.ok(histories);
    }
    @DeleteMapping("/{sessionId}/history")
    public ResponseEntity<String> DelHistoryBySessionId(@PathVariable String sessionId){
        chatMemoryService.delByMemoryId(sessionId);
        return ResponseEntity.ok("删除成功");
    }
}
