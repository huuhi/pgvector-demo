package com.huzhijian.pgvectordemo.demo1;

import com.zaxxer.hikari.HikariConfig;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.pgvector.DefaultMetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageMode;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/26
 * 说明:
 */
@Component
public class PgVectorEmbeddingFactory {
//    获取Pg
//    @Value("${embedding.host}")
//    private  String host;
//    @Value("${embedding.database}")
//    private  String database;
//    @Value("${embedding.user}")
//    private  String user;
//    @Value("${embedding.password}")
//    private  String password;
//    @Value("${embedding.port}")
//    private  Integer port;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    HikariConfig config = new HikariConfig();
    @Bean
    public Function<String,PgVectorEmbeddingStore> getPgVector(EmbeddingModel embeddingModel){
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        return table->PgVectorEmbeddingStore.datasourceBuilder()
                .datasource(config.getDataSource())
                .table(table)
                .dimension(embeddingModel.dimension())
                .createTable(true)
                .metadataStorageConfig(DefaultMetadataStorageConfig.defaultConfig())
                .build();
    }

}
