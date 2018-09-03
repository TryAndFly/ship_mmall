# ship_mmall

项目名称：购物商场项目
开发环境：IDEA+Tomcat+Mysql+Git
开发工具:Spring+MyBatis+SpringMVC+Maven+Nginx+Redis
项目描述:此项目主要实现的功能是用户网上购买商品功能，包括交易系统和后台管理系统、支付系统、单点登录系统。
功能模块有：商品模块、购物车模块、收货地址模块、订单模块、用户模块、支付模块及相关的后台管理功能模块

技术描述：
1.使用maven工具对项目依赖、项目构建、项目信息进行管理
2.此项目采用B/S架构，MVC模式，基于SSM框架实现
3.登录模块忘记密码功能使用token防止横向越权，商品模块使用pageHelper实现了动态排序和分页显示功能，接入支付宝当面付接口完成支付模块功能。
4.使用nginx实现负载均衡，使用redis实现缓存
5.使用Redis+Cookie+Jackson+Filter实现单点登录功能
6.Spring Schedule+Redis分布式锁构建分布式任务调度实现定时关闭订单功能
