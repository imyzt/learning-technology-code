## 整体概括
python + flask 快速搭建web服务器， 包含：
1. 通过flask实现web endpoint
2. 通过`PyMySQL`快速连接数据库，实现crud
3. 通过`APScheduler`，实现定时任务
4. 通过`logging`模块打印日志
5. 包含`Dockerfile`
6. 包含可运行服务器`gunicorn`, `gunicorn -w 4 dbmysql:app`

## 根据本项目依赖生成`requ`
1. 安装 `pipreqs`
`pip install pipreqs`
2. 本项目目录下运行 `pipreqs`

## 表结构模型：
```sql
create table user
(
    id      int auto_increment
        primary key,
    name    varchar(32) not null,
    created timestamp   not null
);
```

## web 请求测试
```shell
curl 'http://localhost:5001/list'
```