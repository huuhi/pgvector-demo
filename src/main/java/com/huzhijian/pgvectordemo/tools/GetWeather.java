package com.huzhijian.pgvectordemo.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/28
 * 说明:
 */
@Slf4j
@Component
public class GetWeather {

    @Tool("获取指定城市某个日期的天气")
    public String getWeather(@P("城市名称")String city,@P("日期")String date){
        log.info("调用了getWeather");
        return date+","+city+"阴天";
    }
//    获取今天的日期
    @Tool("获取今天的日期")
    public String getDate(){
        log.info("调用了getDate");
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
