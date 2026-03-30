package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.service.DirectiveAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/30
 * 说明: 命令授权控制器
 */
@RestController
@Slf4j
@RequestMapping("/directive")
public class DirectiveAuthController {
    private final DirectiveAuthService directiveAuthService;

    public DirectiveAuthController(DirectiveAuthService directiveAuthService) {
        this.directiveAuthService = directiveAuthService;
    }

//    添加命令行权限
    @PostMapping("/{sessionId}/auth")
    public ResponseEntity<String> Auth(@PathVariable String sessionId, @RequestParam String directive){
        directiveAuthService.addAuth(sessionId,directive);
        return ResponseEntity.ok("OK!");
    }

}
