## 基于LangChain4J+Postgresql和PgVector实现的基础对话流AI

主要目的在于学习LangChain4J和PgVector

#### 3.27
完成基本的向量检索
支持文件上传，文件分开，向量化之后存储到知识库中

发现的问题: 文件太大，可能会导致向量化失败，可以考虑循环分批进行向量化 