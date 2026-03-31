package com.huzhijian.pgvectordemo.tools;

import com.huzhijian.pgvectordemo.domain.entity.DirectiveAuth;
import com.huzhijian.pgvectordemo.service.DirectiveAuthService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/30
 * 说明:
 */
@Slf4j
@Component
public class ExecutionDirectiveTool {
    @Value("${skills-path.win}")
    private  String skillPath;
    private final DirectiveAuthService directiveAuthService;

    public ExecutionDirectiveTool(DirectiveAuthService directiveAuthService) {
        this.directiveAuthService = directiveAuthService;
    }

//    下载skill,理论上还需要用户的ID
    @Tool("下载skill")
    public String installSkill(@P("下载链接")String url,@P("skill的文件夹名称(通常为skill名称)")String skillName){
        try {
            log.info("下载:{}",url);
            String s = new ProcessExecutor()
                    .command("cmd.exe", "/c", "npx", "skills", "add", url, "--agent","claude-code","-p","-y")
                    .timeout(20, TimeUnit.SECONDS)
                    .readOutput(true)
                    .execute().outputUTF8();
            log.info("控制台消息：{}",s);
        } catch (Exception e) {
            log.error("错误:{}",e.getMessage());
            return "下载失败,错误消息"+e.getMessage();
        }
//        try {
//            FileUtils.copyDirectory(new File(skillPath+"/.agents/skills/"+skillName),new File(skillPath+skillName));
//        } catch (IOException e) {
//            log.error("错误:{}",e.getMessage());
//            return "错误消息:"+e.getMessage();
//        }
        return "下载成功";
    }

    @Tool("执行命令,CMD命令行.注意一些命令可能会进入交互，可使用--help查看如何添加参数,而不进入交互模式，比如: ")
    public String executionDirective(@P("执行的命令")String directive,@P("会话ID")String sessionId){
        //  检查是否有非法字符
        String result = strictCheck(directive);
        if (!result.isEmpty()){
            return result+"是非法字符";
        }
        //        这里需要去对命令过滤，只执行允许的命令
        log.info("执行命令:{}",directive);
        if (getAuth(sessionId,directive)){
            String s;
            try {
                s = new ProcessExecutor()
                        .command("cmd.exe", "/c",directive)
                        .readOutput(true)
                        .timeout(20, TimeUnit.SECONDS)
                        .execute()
                        .outputUTF8();
            } catch (Exception e) {
                log.error("错误:{}",e.getMessage());
                return "执行命令时出现错误:"+e.getMessage();
            }
            return s;
        }
        return "不允许执行的命令，请让用户授权!";
    }
    private String strictCheck(String directive){
        String[] blackList={";","&","|",">","<","$","`","\n"};
        for (String s : blackList) {
            if (directive.contains(s)){
//                非法
                return s;
            }
        }
//        合法
        return "";
    }
    private boolean getAuth(String sessionId,String directive){
        List<DirectiveAuth> list = directiveAuthService
                .lambdaQuery()
                .eq(DirectiveAuth::getMemoryId, sessionId)
                .list();
        if (list.isEmpty()) return false;
//        正则判断, ls *,npm *,npm install
        for (DirectiveAuth auth : list) {
            String authDirective = auth.getDirective().trim();
            String regex = wildcardToRegex(authDirective);
            if (directive.matches(regex)) return true;
        }
        return false;
    }

    private String wildcardToRegex(String rule) {
        // 1. 转义正则中的特殊字符（防止 rule 里包含 . + ? 等）
        // 2. 将 * 替换为 .* (匹配任意字符)
        // 3. 加上前后锚点，确保完全匹配
        String escaped = rule.replace(".", "\\.")
                .replace("?", "\\?")
                .replace("+", "\\+");

        // 把 * 变成 .*，并处理空格
        String regex = escaped.replace("*", ".*");

        return "^" + regex + "$";
    }

}
