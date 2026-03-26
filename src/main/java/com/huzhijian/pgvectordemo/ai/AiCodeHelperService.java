package com.huzhijian.pgvectordemo.ai;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.service.SystemMessage;
import org.springframework.stereotype.Service;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/25
 * 说明:
 */
@Service
public interface AiCodeHelperService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(UserMessage msg);

}
