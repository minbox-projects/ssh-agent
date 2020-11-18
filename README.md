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
  <version>1.0-SNAPSHOT</version>
</dependency>
```

**Gradle方式：**

在`build.gradle`文件内添加如下依赖：

```
implementation 'org.minbox.framework:ssh-agent:1.0-SNAPSHOT'
```

## 为什么要用？

服务器的安全性、数据的安全性对于我们来说异常的重要，有时服务器的运维人员仅通过`SSH PublicKey`给我们开通权限，我们只能通过已授权的公钥访问服务器，这种方式安全系数比较高。

如果我们需要访问通过公钥访问的MySQL数据库时，服务器的防火墙并未开通`3306`（MySQL默认端口号）的入口访问，那我们该怎么在本机访问远程的数据库呢？

如果是数据库图形界面工具很简单就可以连接，因为他们都提供了基于SSH的连接方式，那如果我们想在本地的项目中使用远程数据库呢？该怎么去做呢？

那么请尝试使用`ssh-agent`。

## 实现原理

`ssh-agent`是通过建立本机与远程服务器之间的端口转发的形式来做的，类似于Docker的**宿主机**与**容器**之间的通信方式。

首先需要登录到远程的服务器，`ssh-agent`目前支持两种方式：

**用户名密码方式：** 默认通过22端口号，通过提供的服务器用户名、密码来建立有效的通信会话

**用户秘钥方式：** 默认通过22端口号，通过用户名以及当前用户`home`目录下的`ssh private key`文件来建立安全有效的通信会话（默认方式）。

**登录成功后，会建立指定的本机端口号与远程服务器端口号的转发通信管道。**

## AgentConfig

## AgentConnection