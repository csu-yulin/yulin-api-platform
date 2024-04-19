# backend

## 大致内容：

aop：

- 环绕advice，打印请求简略信息

common：

- 通用响应类
- 通用请求类（DTO）

config：

- 跨域配置
- mp分页请求配置
- mp自动注入值配置（创建人id）
- redis序列化配置
- ==Spring security配置==
- swagger文档配置

constant：

- 常量

controller：

- api：CRUD和上线和下线接口，在增加一个接口时，==会通过一个简单的http工具类来调用这个接口判断它是否可以可用同时自动生成这个请求的SDK富参数模型==
  - TODO：interface项目部署到内网，http调用工具应进行相应的调整
- api元数据：CRUD，未实现
  - TODO：api元数据更改，其实也会导致SDK代码的生成，甚至全部重置
- api-log：CRUD，==报表统计（牵扯到离线缓存和日志持久化）ps：监听MySQL数据的变化==
- user-api：CRUD，统计接口调用次数
- user：CRUD

enums：

- 枚举，传入字符返回对应的枚举常量、传入字符判断是否有这个字符的枚举常量

exception：

- 异常基类，自定义异常类
- 异常基类继承runtimeexception，自定义异常类继承基类，全局异常类处理基类，需要抛出异常的地方，抛出自定义异常类，同时注入响应枚举类（继承了响应接口），全局异常类处理后向客户端返回错误响应

filter：

- ==jwt认证过滤器==

handler：

- 全局异常处理器，同时也包含了处理spring security认证和鉴权失败的两个处理方法
  - 不过其实应该分开写的
  - TODO：在过滤层抛出的异常其实并不会被controller advice处理，反而会被spring security认证和鉴权失败的两个处理方法进行处理

job：

- ==每日报表处理==

mapper：

- 主类用了mappercan这些mapper就不用mapper注解了

model：

- entity：数据库实体，使用了swagger3的注解
  - ==UserDetail==这个实体继承了spring security提供的UserDetail，对实现认证和鉴权非常重要
- dto：请求实体
- vo：响应实体

properties：

- SDK属性类， 用与自动生成代码

service.impl：

- 接口都被搬到common项目了（不是包）
- ==UserDetailsServiceImpl这个实现对实现认证和鉴权非常重要==

utils：

- httputils：借助hutool http实现了一个简单的请求封装类
- ==JavaClassGeneratorUtils：配合freemarker模板引擎自动生成java类==
- jwtutils：借助hutool jwt实现了一个jwt的生成和解析＋验证
- KeyGeneratorUtils：用户密钥和私钥生成
- RegexUtils：正则工具包，校验一些字符串的格式
- SqlUtils：防止sql注入和驼峰命名转换
- ThrowUtils：简化异常抛出

templates：

- ==用于生成java类的ftl模板文件==

application.yml：

- 多环境开发

## 亮点剖析：

### 1. Spring Security +JWT 实现认证鉴权（一次认证，次次鉴权）

Spring Security 是基于过滤器链的，它在请求到达应用程序之前和之后执行一系列过滤器。这些过滤器处理认证、授权、会话管理等任务。

其核心组件：

- SecurityContextHolder：持有安全上下文SecurityContext
- SecurityContext：主要作用就是获取Authentication对象
- Authentication：表示用户认证信息，用户的详细信息（UserDetails）和用户鉴权时所需要的信息：用户名和密码之类的
- AuthenticationManager：认证接口，校验Authentication
- ProviderManager：是AuthenticationManager最常见的实现，它也不自己处理验证，而是将验证委托给其所配置的AuthenticationProvider列表，然后会依次调用每一个 AuthenticationProvider进行认证，或者通过简单地返回null来跳过验证。如果所有实现都返回null，那么ProviderManager将抛出一个ProviderNotFoundException。这个过程中只要有一个AuthenticationProvider验证成功，就不会再继续做更多验证，会直接以该认证结果作为ProviderManager的认证结果。
- UserDetails：存储用户信息
- UserDetailsService：通过loadUserByUsername来获取UserDetails

Case 1 未登录：

1. JWT过滤器未检测到携带token，放行
2. 创建Authentication对象并将其传入AuthenticationManager对象（框架默认提供的provider）的authenticate方法进行验证
3. 调用loadUserByUsername获取UserDetails对象（能够获取已经代表成功了）
4. 生成JWT返回

Case 2 已登录

1. JWT过滤器检测到携带token，进行JWT校验和解析
2. 获取UserDetails对象
3. 设置Authentication
4. 执行过滤器链中的下一个



### 2. 每日报表统计

在数据量大的情况下，使用 Spring Scheduler定时任务每日执行报表统计功能，统计每日接口的调用情况，将结果集存入Redis，并进行持久化操作（生成Excel表格），管理员需要进行报表统计功能时，优先会从Redis中直接读取结果，若缓存失效，则才进行实时计算



### 3. SDK代码自动生成

基于代码语料自动生成代码：需要“语料”+“规则”两个核心元素

- 从开放平台实现的零编码API网关底层获取API元数据，解析API元数据获取将生成类的包名、类名、描述、参数列表、import列表等，然后编写DSL语料模板，最后执行基于DSL的语言转换引擎即可生成代码（CICD）

简化版：

- API元数据通过配置文件（包名等共有不易改变的信息）和数据库存储（类名、字段名、列表名等独有的）来获取
- 使用freemarker模板引擎编写代码模板
  - thymeleaf适合做服务器端渲染
  - FreeMarker与Web容器无关，来生成文本输出，还可以用于生成XML，JSP或Java 等。
- 注入API元数据执行模板引擎生成SDK参数富模型类（到相应的文件下去）

# SDK

通信模块：

- http调用接口，提供get和post方法
- 提供四个实现类：okhttp、httpclient、HttpURLConnection 、hutool http工具（做了一层封装）

序列化模块（jackson）：

- 序列化：对一些数据类进行自定义序列化（LocalDateTime）
- 反序列化：对返回的json进行拆分以满足数据要求

安全模块：

- API安全签名（在网关层进行校验）
  - 可校验请求者的合法性
    - accessKey+secretKey
  - 可以校验参数的完整性
    - 请求参数+请求体（base64编码）
  - 可以防止重放攻击
    - 先加随机数防止重放攻击，不可能将随机数永久存储，所以使用redis存储并设置过期时间，为了防止随机数过期后被删除时再次被重放攻击，所以加入了时间戳，来保证这次请求只在一定的时间内有效
- 请求方法+请求地址+accessKey+secretKey+时间戳+随机数+请求参数+请求体（base64编码）+版本号形成字符串，计算签名
- 发送时会携带请求头：accessKey、时间戳、随机数、签名字符串、签名方法

应用模块：

- 为用户提供统一接口YulinApiSdkClient，只需传入请求对象，执行execute方法即可一键调用API
- 参数富模型API：为每一个API生成请求对象、响应对象、示例对象，用户构造请求对象，用对应的响应对象接收即可



starter：

- 导入依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure-processor</artifactId>
    <optional>true</optional>
</dependency>
```

- 创建client的AutoConfiguration类，打上@AutoConfiguration注解，添加condition条件

```java
@AutoConfiguration
@ConditionalOnClass(YulinApiSdkClient.class)
@EnableConfigurationProperties(value = YulinApiSdkClient.class)
public class YulinApiSdkAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public YulinApiSdkClient yulinApiSdkClient() {
        return new YulinApiSdkClient(new HutoolHttpClient());
    }
}
```

- 在`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`文件中存入自动配置类的路径
- Maven install即可
- 同时使用spring-boot-configuration-processor依赖结合ConfigurationProperties注解来实现配置字段的代码提示、错误检查和自动赋值（YulinApiSdkClient这个类的字段：accessKey、secretKey、请求地址、加密方法）

==这样，在项目中通过pom文件引入SDK，在配置文件里配置密钥等信息，使用Autowire或者Resource自动注入YulinApiSdkClient对象就直接可以使用了==

# gateway

<font style="font-size:20px">网关</font>

系统的架构大致经历了：单体应用架构—> SOA 架构—>微服务架构的演变

网关是位于客户端和有多个服务组件构成的服务系统之间的中间件

==API 网关为客户与服务系统之间的交互提供了统一的接口，也是管理请求和响应的中心点==

- 网关主要做了两件事情：**请求转发** + **请求过滤**。
  - 请求转发、安全认证（身份/权限认证）、流量控制、负载均衡、降级熔断、日志、监控、参数校验、协议转换等
- Zuul 、SpringCloud Gateway、OpenResty 、Kong 



<font style="font-size:20px">Spring Cloud Gateway</font>

Spring Cloud Gateway 基于 Filter 链的方式提供了网关基本的功能

- 路由、断言、过滤器

![Spring Cloud Gateway 的工作流程](https://oss.javaguide.cn/github/javaguide/system-design/distributed-system/api-gateway/spring-cloud-gateway-workflow.png)客户端的请求先通过匹配规则（Gateway Handler Mapping )找到合适的路由，就能映射到具体的服务。然后请求经过过滤器（Gateway Web Handler)处理后转发给具体的服务，服务处理后，再次经过过滤器处理，最后返回给客户端



过滤器执行顺序：

- GlobalFilter 通过实现 Ordered 接口，或者使用 @Order 注解来指定 order 值，由我们自己指定。
-  路由过滤器和 defaultFilter 的 order 由 Spring 指定，默认是按照声明顺序从1递增。 
- order相同时：
  - 两个GlobalFilter类型的过滤器Order值相同时，根据文件名字母排序，文件名靠前的优先更高。
  - GlobalFilter类型和GatewayFilter类型的过滤器Order值相同时，GlobalFilter类型优先更高。

## 大致流程：

使用到的过滤器：

- RewritePath：局部过滤器，重写路径
- PrintUrlFilter：全局过滤器，打印完整转发地址
- IPFilter：局部过滤器，黑白名单过滤
- RequestRateLimiter：局部过滤器，限流
- AuthenticationFilter：局部过滤器，权限校验
- CacheBodyGlobalFilter：全局过滤器，缓存请求体使其可以多次读取
- RequestLogFilter：全局过滤器，请求日志持久化
- CircuitBreaker：局部过滤器，熔断降级



两组路由，一组匹配/api/**，另一组匹配所有（后匹配）

- 如果没有匹配上/api/**，那么返回错误信息

匹配上了/api/**：

RewritePath：

- 重写路径为http://localhost:8081/api/~~~~~~~~~~~~

PrintUrlFilter：

- 打印完整转发地址

IPFilter：

- 黑白名单过滤

RequestRateLimiter：

- 每秒能处理1个请求，最多允许并发处理三个请求
- 使用请求头中携带的用户密钥作为限流的key
- 令牌桶算法：系统维护一个固定容量的令牌桶，其中以固定速率往桶里放入令牌（token）。每当一个请求到达时，它必须从令牌桶中获取一个令牌才能继续处理。如果令牌桶中没有足够的令牌，则请求将被暂时阻塞或者拒绝，直到令牌桶中有足够的令牌为止。

AuthenticationFilter：

- 进行API安全认证
  - 通过用户的请求，获取签名的字段，重新进行签名，和发过来的签名进行比较
- 有效性校验：
  - 判断用户是否存在（密钥）
  - 判断接口是否存在（请求路径）
  - 判断用户是否购买了接口以及是否还有接口调用次数
- ==使用到了Dubbo和nacos==

CacheBodyGlobalFilter：

- 缓存请求体使其可以多次读取

RequestLogFilter：

- 请求日志持久化
- ==使用到了Dubbo和nacos==

CircuitBreaker：

- 断路器，当请求返回的状态码为 `BAD_GATEWAY` 或者 `BAD_REQUEST` 时，断路器都会触发，执行相应的降级逻辑。



# Dubbo和nacos

RPC（Remote Procedure Call） 即远程过程调用

- 通过 RPC 可以帮助我们调用远程计算机上某个服务的方法，这个过程就像调用本地方法一样简单，而且我们不需要了解底层网络编程的具体细节。

nacos：

- 服务注册中心

Dubbo ：

- Provider： 暴露服务的服务提供方，会向注册中心注册自己提供的服务。必须。
  - backend：主要提供一些操作数据库的服务
- Consumer： 调用远程服务的服务消费方，会向注册中心订阅自己所需的服务。必须
  - gateway
- Registry： 服务注册与发现的注册中心。注册中心会返回服务提供者地址列表给消费者。非必须。



<font style="font-size:20px">实现过程：</font>

抽取出服务的接口（common），使得二者都能访问到

backend：

导入依赖：

```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>3.3.0-beta.2</version>
</dependency>

<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-registry-nacos</artifactId>
    <version>3.3.0-beta.2</version>
</dependency>

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>2023.0.0.0-RC1</version>
</dependency>
```

启动类上打上：

- @EnableDubbo：开启 Dubbo 在 Spring Boot 中的自动配置
- @EnableDiscoveryClient：开启 Spring Cloud 的服务发现功能

在service的实现类上打上：

- @DubboService



gateway：

导入相同依赖

启动类上打上：@EnableDubbo

再需要用到服务的地方打上：@DubboReference

- 使用 `@DubboReference` 注解标记的字段或者方法参数会被 Dubbo 框架自动注入一个远程服务的代理对象，使得应用能够通过本地调用方式来调用远程服务。

# interface和common

interface：

- 提供API服务调用

common：

- RPC远程调用服务的公用接口



# 访问流程

用户登录backend，浏览接口，购买接口，下载SDK（也可以用Maven导入），申请密钥，在自己的代码中使用sdk访问gateway，通过校验之后，访问内网的interface，返回结果

# 部署

backend，interface，gateway，frontend

服务器1：

gateway：

- nacos1
  - interface1



nginx：

- frontend

redis

服务器2：

backend

mysql

- nacos1
  - interface2

































整体流程