package com.huzhijian.pgvectordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huzhijian.pgvectordemo.domain.ChatHistory;
import com.huzhijian.pgvectordemo.domain.dto.MessageDTO;
import com.huzhijian.pgvectordemo.service.ChatMemoryService;
import com.huzhijian.pgvectordemo.mapper.ChatMemoryMapper;
import dev.langchain4j.data.message.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author windows
* @description 针对表【chat_memory(存储会话记忆)】的数据库操作Service实现
* @createDate 2026-03-25 21:23:32
*/
@Slf4j
@Service
public class ChatMemoryServiceImpl extends ServiceImpl<ChatMemoryMapper, ChatHistory>
    implements ChatMemoryService{

    @Resource
    private ChatMemoryMapper mapper;

    @Override
    public List<ChatHistory> getByMemoryId(Object memory) {
        return mapper.getAllByMemoryId(memory);
    }

    @Override
    public void delByMemoryId(Object memoryId) {
        mapper.delAllByMemoryId(memoryId);
    }

    @Override
    public void insertBatch(List<ChatHistory> list) {
        if (list.isEmpty()){
            log.debug("插入失败,消息为空");
            return;
        }
        mapper.insertBatch(list);
    }
//dev.langchain4j.data.message;
//dev.langchain4j.data.message.ChatMessage
    @Override
    public List<ChatMessage> getHistoryBySessionId(String sessionId) {
        List<ChatHistory> memoryList = mapper.getAllByMemoryId(sessionId);
        if (memoryList==null||memoryList.isEmpty()) return List.of();
        return memoryList.stream().map(m-> ChatMessageDeserializer.messageFromJson(m.getContent())).toList();
    }

    @Override
    public List<MessageDTO> getHistory(List<ChatMessage> history) {


        return history.stream().map(msg->{
            MessageDTO.MessageDTOBuilder messageDTOBuilder = MessageDTO
                    .builder()
                    .type(msg.type().name());
            switch (msg) {
                case UserMessage userMessage -> {
//                  TODO  这里要考虑之后，图文并发的时候，怎么处理
//                    暂时只考虑单文本
                    log.info("userMessage:{}",userMessage);
                    userMessage.contents().forEach(content->{
                        if(content instanceof  TextContent textContent){
                            messageDTOBuilder.content(textContent.text());
                        }
                    });

                    if (userMessage.hasSingleText()) {
                        String text = userMessage.singleText();
                        /*这里获取到了:
                        UserMessage { name = null, contents = [TextContent { text = "text" }], attributes = {} }
                        要进行转换
                        */
                        log.info("内容：{}",text);
                        messageDTOBuilder.content(text);
                    }
//                	"contents": [{
                //		"text": "UserMessage { name = null, contents = [TextContent { text = \"广东职业技术学院张政康的具体信息\" }], attributes = {} }",
                //		"type": "TEXT"
                //	}],
                //	"type": "USER"
                }
                case AiMessage aiMessage -> {
                    messageDTOBuilder.content(aiMessage.text());
                    messageDTOBuilder.thinking(aiMessage.thinking());
//                toolExecutionRequests
                    List<MessageDTO.ToolRequestDTO> requestDTOList = aiMessage.toolExecutionRequests().stream().map(request -> MessageDTO.ToolRequestDTO.builder()
                            .toolName(request.name())
                            .id(request.id())
                            .arguments(request.arguments()).build()).toList();
                    messageDTOBuilder.toolRequestList(requestDTOList);
                }
                case ToolExecutionResultMessage toolResult -> {
                    MessageDTO.ToolResultDTO resultDTO = MessageDTO.ToolResultDTO.builder()
                            .isError(toolResult.isError())
                            .result(toolResult.text())
                            .toolName(toolResult.toolName())
                            .id(toolResult.id())
                            .build();
                    messageDTOBuilder.toolResultDTO(resultDTO);
                }
                default -> throw new IllegalStateException("Unknown Type: " + msg);
            }
            return messageDTOBuilder.build();
        }).toList();
    }

    @Override
    public int getCountBySessionID(String sessionId) {
        return mapper.getCountByMemoryId(sessionId);
    }
}




