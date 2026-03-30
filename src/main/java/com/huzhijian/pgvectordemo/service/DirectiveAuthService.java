package com.huzhijian.pgvectordemo.service;

import com.huzhijian.pgvectordemo.domain.DirectiveAuth;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author windows
* @description 针对表【directive_auth(命令行授权)】的数据库操作Service
* @createDate 2026-03-30 16:45:19
*/
public interface DirectiveAuthService extends IService<DirectiveAuth> {

    void addAuth(String sessionId,String directive);
}
