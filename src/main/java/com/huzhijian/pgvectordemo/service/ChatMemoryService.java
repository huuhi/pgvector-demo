package com.huzhijian.pgvectordemo.service;

import com.huzhijian.pgvectordemo.domain.entity.ChatHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huzhijian.pgvectordemo.domain.vo.MessageVO;

import java.util.List;

/**
* @author windows
* @description 针对表【chat_memory(存储会话记忆)】的数据库操作Service
* @createDate 2026-03-25 21:23:32
*/
public interface ChatMemoryService extends IService<ChatHistory> {
    List<ChatHistory> getByMemoryId(Object memory);
    void delByMemoryId(Object memoryId);
    void insertBatch(List<ChatHistory> list);


    List<MessageVO> getHistory(String sessionId);

    int getCountBySessionID(String sessionId);
}
