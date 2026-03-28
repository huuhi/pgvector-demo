package com.huzhijian.pgvectordemo.config;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/28
 * 说明:
 */
@Configuration
public class McpConfig {

    @Bean(destroyMethod = "close")
    public McpClient modelScopeMcpClient(){
            McpTransport mcpTransport = StreamableHttpMcpTransport.builder()
                .url("https://mcp.api-inference.modelscope.net/6d1c1a7c23664d/mcp")
                .logRequests(true)
                .logResponses(true)
                .timeout(Duration.ofSeconds(60))
                .build();
            return new DefaultMcpClient.Builder().transport(mcpTransport).build();
    }

    @Bean
    public McpToolProvider mcpToolProvider(McpClient mcpClient){
        // 写成提供器
//        第二种
        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .toolSpecificationMapper((client, toolSpec) -> {
                    // Prefix all tool names with "myprefix_" and convert the description to uppercase
                    return toolSpec.toBuilder()
                            .name("myprefix_" + toolSpec.name())
                            .description(toolSpec.description().toUpperCase())
                            .build();
                })
                .build();
        //  第一种写法：
        return McpToolProvider.builder()
                .mcpClients(mcpClient)
                .toolNameMapper((client,toolSepc)->client.key()+"_"+toolSepc.name())
                .build();

    }
}
