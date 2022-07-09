1. 本地编译镜像需要maven配置选项增加
-DsendCredentialsOverHttp=true
2. 通过网关访问open api地址:
http://127.0.0.1:8083/framework-openapi/swagger-ui/index.html
3. 服务中间件编排使用docker-compose
a. 当前目录下执行 docker-compose up -d 即可启动所有依赖中间件
b. nacos 配置初始化,登陆mysql 地址: 127.0.0.1:3306 密码:root@123 使用nacos-mysql.sql 初始化即可

