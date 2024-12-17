# This is a Http server demo  That based on java NIO

## 基于javaNio的HTTP服务器
项目未完成开发。
以实现功能：

GET POST实现

JavaServlet 过滤器、Session、请求、响应实现。可挂载简单的JavaServlet应用。

Reactor线程模型

伙伴算法内存分配器
该项目使用JavaNio和主从Reactor线程模型解决了在阻赛I/O下需要大量创建线程，或使用线程池从而引起CPU上下文频繁切换所引起的性能问题。

项目线程模型模仿的是Netty，项目线程模型分为一个接收者线程，一个任务分发线程，多个工作者线程，以及一个带有拒绝策略的有界线程池。

项目模仿了Netty框架的内存分配，采用了数组实现的伙伴算法进行简单的内存分配和回收。避免了频繁请求堆外内存造成的内存泄露，同时避免了JVM二次拷贝带来的性能损耗。

该项目模仿了Tomcat的生命周期管理对服务器进行管理。服务器初始化由外到内，服务器关闭由内到外，服务器内部异常抛出由内到外统一记录和判断，严重级别异常会触发服务器关闭事件。

该项目实现了部分JavaServlet规范，可扫描并运行JavaServlet应用。实现了Servlet容器、过滤器、HttpServletRequest、HttpServletResponse接口、Session管理。

该项目实现了O(n)时间复杂度和O(1)空间复杂度的Http解析器，可以提取出HTTP请求行、请求头、请求体字段。满足客户端与服务端基本的数据传输，图片、js、json、查询字符串等。

该项目引入了静态文件缓存，采用了基于双向链表实现的LRU缓存替换算法，有效复用了系统内存并提高了系统对静态资源的响应速度。

## 未来：
引入Raft算法、Protobuffe数据格式、数据压缩、实现RPC、实现stun协议、实现负载均衡反向代理、插件化。
## 最终？
一个全能的高性能服务器，只需要简单的配置，就可以成为网关、代理服务器、消息队列、应用程序服务器、stun服务器或客户端、内存数据库、争取实现一次“编译”到处运行，结束繁杂的各种环境的搭建。
