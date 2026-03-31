package com.huzhijian.pgvectordemo.service;

import com.huzhijian.pgvectordemo.domain.entity.McpSkillInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huzhijian.pgvectordemo.domain.vo.McpSkillVO;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.skills.Skills;

import java.util.List;

/**
* @author windows
* @description 针对表【mcp_skill_information(存储MCP和SKILL的信息)】的数据库操作Service
* @createDate 2026-03-31 15:09:59
*/
public interface McpSkillInformationService extends IService<McpSkillInformation> {

    Skills getSkills(List<String> skills);

    McpToolProvider getMcp(List<String> mcPs);

    void add(List<McpSkillInformation> list);
}
