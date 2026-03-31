package com.huzhijian.pgvectordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huzhijian.pgvectordemo.domain.entity.McpSkillInformation;
import com.huzhijian.pgvectordemo.domain.vo.McpSkillVO;
import com.huzhijian.pgvectordemo.service.McpSkillInformationService;
import com.huzhijian.pgvectordemo.mapper.McpSkillInformationMapper;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.skills.DefaultFileSystemSkill;
import dev.langchain4j.skills.Skill;
import dev.langchain4j.skills.Skills;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
* @author windows
* @description 针对表【mcp_skill_information(存储MCP和SKILL的信息)】的数据库操作Service实现
* @createDate 2026-03-31 15:09:59
*/
@Service
@Slf4j
public class McpSkillInformationServiceImpl extends ServiceImpl<McpSkillInformationMapper, McpSkillInformation>
    implements McpSkillInformationService{

    @Override
    public Skills getSkills(List<String> skills) {
        if (skills==null||skills.isEmpty()) return null;
        List<McpSkillInformation> skillList= lambdaQuery().eq(McpSkillInformation::getIsMcp, false)
                .list();

        List<Skill> loadSkillList= new ArrayList<>(List.of());
        if (skillList.isEmpty()) return null;
//
        for (McpSkillInformation information:skillList){
            if (skills.contains(information.getName())) {
//            如果包含说明是需要这个skill的，添加
                Path path = Path.of(information.getSourcePath());
//                先看看这个路径存在不
                if (!path.toFile().exists()) {
                    continue;
                }
                DefaultFileSystemSkill s = DefaultFileSystemSkill.builder().basePath(path).build();
                loadSkillList.add(s);
                log.debug("一个SKILL加载成功！");
            }
        }
        return Skills.from(loadSkillList);
    }

    @Override
    public McpToolProvider getMcp(List<String> McpNameList) {
        if (McpNameList==null||McpNameList.isEmpty()) return null;
        List<McpSkillInformation> McpList= lambdaQuery().eq(McpSkillInformation::getIsMcp, true)
                .list();
        if (McpList==null||McpList.isEmpty()) return null;
        List<McpClient> mcpClients = new ArrayList<>();
        for (McpSkillInformation information : McpList) {
            if (McpNameList.contains(information.getName())){
//               只支持http
                String mcpType = information.getMcpType();
                if (mcpType.equals("http")){
                    StreamableHttpMcpTransport mcpTransport = StreamableHttpMcpTransport.builder()
                            .url(information.getSourcePath())
                            .timeout(Duration.ofSeconds(10))
                            .build();
                    McpClient mcpClient = DefaultMcpClient.builder().transport(mcpTransport).build();
                    try {
                        mcpClient.listTools();
                    } catch (Exception e) {
//                说明这个MCP服务不可用，一般来说应该在数据库里面标记一下，这里是demo，就不处理了
                        try {
                            mcpTransport.close();
                        } catch (IOException ex) {
                        }
                        continue;
                    }
                    log.debug("一个MCP加载成功！");
                    mcpClients.add(mcpClient);
                }
            }
        }
        if (mcpClients.isEmpty()) return null;
        return McpToolProvider.builder()
                .mcpClients(mcpClients)
                .toolNameMapper((client,toolSep)->client.key()+"_"+toolSep.name())
                .build();
    }

    @Override
    @Transactional
    public void add(List<McpSkillInformation> list) {
        saveBatch(list);
    }


    private boolean isMcp(String url){
        return url.startsWith("http");
    }
}




