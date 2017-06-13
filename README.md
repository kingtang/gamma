# gamma
类似dubbo和motan的分布式服务框架，当前只是一个Alpha版本，具备基本的能力。（本人的练手项目还未重构且未经过全面的功能测试、性能测试、可靠性测试暂时不具备生产环境部署的条件）
# 当前已具备的特性
- 服务注册和发现
- 基本的路由和容错处理
- 线程和故障隔离
- 发布和引用服务
- 隐式传参
- 各层的扩展能力
# 待实现的特性
***
- 分布式跟踪
- 复杂的容错处理
- 异步调用
- 监控中心
- 配置中心
- Rest协议的支持
# 整体架构
***
![arc](http://oi04x12g2.bkt.clouddn.com/arc.png)
- Remoting&ensp;&ensp;&ensp;&ensp; 通信层分为客户端和服务端，提供基本的空闲链路检测、心跳、重连、半包处理功能，基于Netty实现
- Rpc&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;Rpc层提供服务导入、导出、序列化、编解码、线程池、消息封装、服务调用上下文等基础功能
- Connector&ensp;&ensp;&ensp;&ensp;连接器层类似tomcat中的connector概念，这里不同的connector处理不同的协议
- ServiceEngine&ensp;服务引擎负责处理connecotr流入和流出的消息具备负载均衡、容错等功能
- Pipeline&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;数据管道被ServiceEngine集成能够实现数据在Pipeline中的双向流动，其中pipeline中的每个数据处理节点  
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;称为一个Handler，支持在已有的pipeline中扩展自定义Handler
- ServiceCenter&ensp;&ensp;服务中心提供基本的服务注册和发现能力、缓存能力
- Monitor &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;提供服务监控、服务质量统计等功能未断路器提供参考数据
- Config&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;提供分布式配置管理能力
### 私有协议
***
Gamma内部采用的私有的gamma协议通信，gamma协议的描述如下图所示
![protocal](http://oi04x12g2.bkt.clouddn.com/composite.png)
# 快速入门
***
* [Wiki(中文)](https://github.com/kingtang/gamma/wiki/Gamma%E5%BF%AB%E9%80%9F%E6%8C%87%E5%8D%97)
