package com.huzhijian.pgvectordemo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/31
 * 说明:
 */
@Data
@AllArgsConstructor
public class ChatDTO {
    private String msg;
    private String sessionId;
    private List<String> skills;
    private List<String> MCPs;

}
