package com.huzhijian.pgvectordemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huzhijian.pgvectordemo.domain.DirectiveAuth;
import com.huzhijian.pgvectordemo.service.DirectiveAuthService;
import com.huzhijian.pgvectordemo.mapper.DirectiveAuthMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
* @author windows
* @description 针对表【directive_auth(命令行授权)】的数据库操作Service实现
* @createDate 2026-03-30 16:45:19
*/
@Service
public class DirectiveAuthServiceImpl extends ServiceImpl<DirectiveAuthMapper, DirectiveAuth>
    implements DirectiveAuthService{


    @Override
    public void addAuth(String sessionId,String directive) {
        DirectiveAuth directiveAuth = DirectiveAuth.builder().memoryId(sessionId).directive(directive).build();
        save(directiveAuth);
    }
}




