package com.huzhijian.pgvectordemo.demo1;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.pgvector.DefaultMetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import jakarta.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.function.Function;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2026/3/26
 * 说明:
 */
@Component
public class PgVectorEmbeddingFactory {
    @Resource
    private DataSource dataSourceT;
    @Bean
    public Function<String,PgVectorEmbeddingStore> getPgVector(EmbeddingModel embeddingModel){
        return table->PgVectorEmbeddingStore.datasourceBuilder()
                .datasource(dataSourceT)
                .table(table)
                .dimension(embeddingModel.dimension())
                .createTable(true)
                .metadataStorageConfig(DefaultMetadataStorageConfig.defaultConfig())
                .build();
    }

}
