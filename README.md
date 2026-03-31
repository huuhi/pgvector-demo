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

### 3.30

今天把记忆的存储方式修改了一下，之前是全部删除全部插入，现在修改成只插入新增的聊天记录了。
添加了个工具，可以执行命令行，不过要是做成服务器的那种，这个工具肯定要砍掉的，不然肯定被狠狠的攻击了
引入了zt-exec，AI说他有以下优势，确实比原生的好用
```xml
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-pgvector</artifactId>
        <version>1.12.1-beta21</version>
    </dependency>
```
- 防止死锁：原生 ProcessBuilder 如果不手动开启线程去读 InputStream 和 ErrorStream，缓冲区满后进程会卡死。zt-exec 自动帮你处理了这些。 
- 流式 API：链式调用，非常直观。 
- 超时控制：原生 Java 做超时非常麻烦，它只需要 .timeout(10, TimeUnit.SECONDS)。 
- 输出获取：可以直接将结果返回为 String，也可以直接泵（Pump）到控制台或文件。


加入了命令行工具以及自动下载skill的工具，目前测试没啥问题。

### 3.31

处理了一下使用命令行的那个工具问题，以及去除最大消息，修改成token限制。
之后其实也可以考虑一下如果消息太多了，拿个模型去总结一下，问题在于总结之后，之后也是要去动态更新的啊，呃呃呃
那我是不是可以写个工具，加入用户码AI我刚刚不是说了吗，之类的话，AI就去调工具，更新一下总结就行了
大概的思路是这样子的
现在想写个dto，来过滤SKILL和MCP的使用，目前是加载了全部的MCP和SKILL，但是一般情况下，是需要用户选择的。
要实现这个，需要弄个东西来存储MCP和SKILL的消息，也就说需要知道有什么，才能选。
可以考虑使用表或JSON来存储这些数据
> 我选择使用表来存储这些
```sql
create table mcp_skill_information(
    id serial primary key,
    name varchar(255) not null ,
    is_mcp bool not null ,
    mcp_type varchar,
    source_path varchar(255) not null
);
comment on table mcp_skill_information is '存储MCP和SKILL的信息';
comment on column mcp_skill_information.is_mcp is '是否为mcp，如果不是则是skill';
comment on column mcp_skill_information.mcp_type is 'MCP的类型,HTTP或SSE';
```

搞定了，目前MCP只支持http协议的,sse不支持，本地的也不支持
目前测试没啥问题。