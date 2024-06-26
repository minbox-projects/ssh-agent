# ssh-agent

提供SSH代理跳板方式，支持 “用户名 + 公钥”、“用户名 + 密码方式” 认证登录，建立本地端口与远程端口通信管道。

## 如何使用？

`ssh-agent`依赖已经上传到`Maven Central`，不同构件环境使用方式不同。

**Maven方式：**

在`pom.xml`文件内添加如下依赖：

```xml
<dependency>
  <groupId>org.minbox.framework</groupId>
  <artifactId>ssh-agent</artifactId>
  <version>1.0.2</version>
</dependency>
```

**Gradle方式：**

在`build.gradle`文件内添加如下依赖：

```
implementation 'org.minbox.framework:ssh-agent:1.0.2'
```

## 在SpringBoot项目内使用

使用示例项目访问：[api-boot-sample-ssh-agent](https://gitee.com/minbox-projects/api-boot/tree/2.3.x/api-boot-samples/api-boot-sample-ssh-agent)。

**步骤一：添加依赖**

在`pom.xml`文件内添加`api-boot-starter-ssh-agent`依赖：

```xml
<dependency>
  <groupId>org.minbox.framework</groupId>
  <artifactId>api-boot-starter-ssh-agent</artifactId>
</dependency>
```

> 如果添加的依赖找不到或者无法下载，请访问官网快速开始：[快速使用手册](https://apiboot.minbox.org/zh-cn/docs/quick-start.html)

**步骤二：配置转发代理端口**

在`application.yml`配置文件内进行配置，可以通过结合spring profiles激活不同配置环境的代理方式

```yaml
# ApiBoot相关配置
api:
  boot:
    # 配置连接生产环境的SSH Agent
    ssh-agent:
      configs:
        # 连接生产MySQL
        - username: metadata
          server-ip: xxx.xxx.xx.xx
          local-port: 3307
          forward-target-port: 3306
        # 连接生产Mongo
        - username: metadata
          server-ip: xxx.xxx.xx.xx
          local-port: 27018
          forward-target-ip: 192.168.1.220
          forward-target-port: 27017
        # 连接生产Redis
        - username: metadata
          server-ip: xxx.xxx.xx.xx
          local-port: 6378
          forward-target-port: 6379
```

> 注意事项：如果不通过username/password方式代理认证，需要在远程服务登录用户Home目录下`~/.ssh/authorized_keys`文件内添加本机的SSH 公钥。

## 在Junit5中使用

**步骤一：继承SshAgentJunitTest单元测试类**

```java
@SpringBootTest
class SimulationServiceApiApplicationTests extends SshAgentJunitTest {
    @Test
    void contextLoads() {
        //...
    }
}
```

> 单元测试类继承自`SshAgentJunitTest`后就可以在测试方法执行前开启ssh代理连接，执行后关闭代理连接。

**步骤二：添加`ssh-agent.yml`配置文件**

`ssh-agent.yml`用于配置ssh代理连接列表，位于`src/test/resources`目录下，如果不创建该配置文件运行单元测试方法时会抛出异常。

```yaml
configs:
  # 代理转发MySQL
  - username: developer
    serverIp: x.x.x.x
    localPort: 3306
    forwardTargetPort: 59500
  # 代理转发Redis
  - username: developer
    serverIp: x.x.x.x
    localPort: 6379
    forwardTargetPort: 59504
  # 代理转发mongo
  - username: developer
    serverIp: x.x.x.x
    localPort: 27017
    forwardTargetPort: 59503
```



## 为什么要用？

服务器的安全性、数据的安全性对于我们来说异常的重要，有时服务器的运维人员仅通过`SSH PublicKey`给我们开通权限，我们只能通过已授权的公钥访问服务器，这种方式安全系数比较高。

为了安全起见，服务器的防火墙一般不会直接开放服务端口（如：MySQL => 3306），加上运维人员是通过`Public Key`的方式给我们授权，那我们该怎么在本机访问远程的数据库呢？

如果是数据库图形界面工具（DataGrip、Navicat）很简单就可以连接，因为他们都提供了基于SSH的连接方式，那如果我们想在本地的项目中使用远程数据库呢？该怎么去做呢？

那么请尝试使用`ssh-agent`。

`ssh-agent`所能代理的不仅仅是MySQL => 3306，是服务器本身上 + 可通信局域网服务器的全部端口号的服务，比如：Mongo => 27017、Redis => 6379等。

## 实现原理

`ssh-agent`是通过建立本机与远程服务器之间的端口转发的形式来做的，类似于Docker的**宿主机**与**容器**之间的通信方式。

首先需要登录到远程的服务器，`ssh-agent`目前支持两种方式：

**用户名密码方式：** 默认通过22端口号，通过提供的服务器用户名、密码来建立有效的通信会话

**用户秘钥方式：** 默认通过22端口号，通过用户名以及当前用户`home`目录下的`ssh private key`文件来建立安全有效的通信会话（默认方式）。

**登录成功后，会建立指定的本机端口号与远程服务器端口号的转发通信管道。**

## AgentConfig

`AgentConfig`类中定义了代理连接远程服务器的全部参数。

| 参数名               | 默认值                       | 描述                                                         |
| -------------------- | ---------------------------- | ------------------------------------------------------------ |
| authenticationMethod | SSH_PRIVATE_KEY              | 登录服务器的授权方式，默认为private key方式                  |
| username             | -                            | 登录服务器的用户名                                           |
| password             | -                            | 登录服务器的密码，仅在USERNAME_PASSWORD授权登录方式下有效    |
| serverIp             | -                            | 服务器IP地址                                                 |
| sshPort              | 22                           | SSH端口号，默认为22                                          |
| sshPrivateKeyPath    | 当前用户home目录/.ssh/id_rsa | 默认使用RSA方式的private key，如果本机通过其他加密方式生成ssh秘钥文件，请修改该参数（请配置文件绝对路径）。仅在SSH_PRIVATE_KEY授权登录方式下有效。 |
| sshKnownHostsPath    | 当前用户home目录/known_hosts | 已知的主机文件，仅在SSH_PRIVATE_KEY授权登录方式下有效。      |
| localPort            | -                            | 本地端口号，最大值：65535                                    |
| forwardTargetPort    | -                            | 转发的目标端口号，最大值：65535                              |
| forwardTargetIp      | 127.0.0.1                    | 转发的目标IP地址，默认为：127.0.0.1（本机），如果需要通过局域网IP地址访问其他服务器需要进行修改 |
| addition             | -                            | 附加的配置参数集合，用于JSch Session#config方法              |



## AgentConnection

`AgentConnection`是用于本机与远程服务器建立连接的核心类，需要通过`AgentConfig`对象实例进行初始化。

> 该接口提供了`#connect`、`#disconnect`两个方法，默认的实现类为`org.minbox.framework.ssh.agent.jsch.JSchAgentConnection`。

**Ssh Private Key连接示例：**

```java
/**
  * 使用ssh private key方式代理连接远程服务
  * <p>
  * 本地 "3307" 端口号绑定远程服务器 "3306"端口号，实现本地访问远程服务器上的MySQL服务
  */
static void sshPrivateConnect() {
  AgentConfig config2 = new AgentConfig();
  config2.setServerIp("xxx.xxx.xxx.xxx");
  config2.setUsername("root");
  config2.setLocalPort(3307);
  config2.setForwardTargetPort(3306);
  AgentConnection connection2 = new DefaultAgentConnection(config2);
  connection2.connect();
  // 连接成功后，访问本地3307的数据库，其实就是访问远程服务器的3306
}
```

**用户名密码连接示例：**

```java
/**
  * 使用用户名密码方式代理连接远程服务
  * <p>
  * 本地 "3307" 端口号绑定远程服务器 "3306"端口号，实现本地访问远程服务器上的MySQL服务
  */
static void usernamePasswordConnect() {
  AgentConfig config = new AgentConfig();
  config.setServerIp("xxx.xxx.xxx.xxx");
  config.setUsername("root");
  config.setPassword("密码");
  config.setLocalPort(3307);
  config.setForwardTargetPort(3306);
  AgentConnection connection = new DefaultAgentConnection(config);
  connection.connect();
  // 连接成功后，访问本地3307的数据库，其实就是访问远程服务器的3306
}
```