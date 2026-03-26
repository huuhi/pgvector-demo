package com.huzhijian.pgvectordemo.mapper;

import com.huzhijian.pgvectordemo.domain.ChatMemory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.javassist.bytecode.annotation.CharMemberValue;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
* @author windows
* @description 针对表【chat_memory(存储会话记忆)】的数据库操作Mapper
* @createDate 2026-03-25 21:23:32
* @Entity com.huzhijian.pgvectordemo.domain.ChatMemory
*/
@Mapper
public interface ChatMemoryMapper extends BaseMapper<ChatMemory> {
    List<ChatMemory> getAllByMemoryId(Object memoryId);

    void delAllByMemoryId(Object memoryId);

    boolean insertBatch(List<ChatMemory> list);
}




