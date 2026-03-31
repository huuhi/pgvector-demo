package com.huzhijian.pgvectordemo.controller;

import com.huzhijian.pgvectordemo.domain.entity.McpSkillInformation;
import com.huzhijian.pgvectordemo.service.McpSkillInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/31
 * 说明:
 */
@RestController
@RequestMapping("/mcp-skill")
public class McpSkillController {

    private final McpSkillInformationService mcpSkillInformationService;

    public McpSkillController(McpSkillInformationService mcpSkillInformationService) {
        this.mcpSkillInformationService = mcpSkillInformationService;
    }

    @PostMapping
    public ResponseEntity<String> addMcpOrSkill(@RequestBody List<McpSkillInformation> list){
        mcpSkillInformationService.saveBatch(list);
        return ResponseEntity.ok("添加成功!");
    }
    @GetMapping
    public ResponseEntity<List<McpSkillInformation>> list(@RequestParam Boolean isMcp){
        List<McpSkillInformation> list = mcpSkillInformationService
                .lambdaQuery().eq(McpSkillInformation::getIsMcp, isMcp)
                .list();
        return ResponseEntity.ok(list);
    }
}
