## 基于LangChain4J+Postgresql和PgVector实现的基础对话流AI

主要目的在于学习LangChain4J和PgVector

### 3.27

完成基本的向量检索
支持文件上传，文件分开，向量化之后存储到知识库中

发现的问题: 文件太大，可能会导致向量化失败，可以考虑循环分批进行向量化 

### 3.28

今天引入了MCP和SKILL，然后思考了一些问题

- 这些MCP和SKILL应该是可以选择性使用的，之后如果要做成多用户，应该要根据用户来进行隔离，比如用户A没有下载SKILLA，那就不能使用。
- 要集成一个下载功能，实现用户给个链接，就可以下载到用户的工作区，并且源文件要存储在一个地方，存储到表里，下次有人来下载同一个SKILL就可以直接复制，不需要引用了
- 命令要怎么执行，怎么实现暂停？白名单，用什么来隔离？是目录还是会话？我记得目录好一点
- 工作目录大概是这样子的:workplace/user/.. 目录下放着skill，mcp配置等信息，然后命令白名单建一张表来存储可能好一点
- 还有就是要返回工具调用的过程，让AI的操作透明化
- 返回历史记录这里可能还需要改进一下

### 3.29

今天把返回历史记录的问题搞定了，不过目前只支持文本，如果之后要图文并发那还要再处理一下
AI的那个接口
```java
import dev.langchain4j.data.message.UserMessage;

TokenStream chat(@UserMessage UserMessage msg, @MemoryId Object memoryId);
```
JSON是这样子:
```json
{
  "contents": [{
    "text": "UserMessage { name = null, contents = [TextContent { text =\"那帮我看看今天佛山天气怎么样\" }], attributes = {} }",
    "type": "TEXT"
  }],
  "type": "USER"
}
```

这样子写会出现嵌套的情况，会很难受，导致用户的消息变成这样子:
```text
UserMessage { name = null, contents = [TextContent { text = "测试，你只需要回复SUCCESS" }], attributes = {} }
```
修改成字符串了，已老实，之后再考虑图文并发吧


