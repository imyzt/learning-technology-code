# 使用官方 Python 镜像作为基础镜像
FROM python:3.8-slim

# 设置工作目录
WORKDIR /app

# 将 requirements.txt 文件复制到工作目录中
COPY requirements.txt .

# 使用 pip 安装 Python 依赖
RUN pip install --no-cache-dir -r requirements.txt -i https://pypi.mirrors.ustc.edu.cn/simple/
RUN pip install gunicorn==20.0.4 -i https://pypi.mirrors.ustc.edu.cn/simple/

# 将项目文件复制到工作目录中
COPY . .

# 暴露容器的 5000 端口（Flask 应用默认端口）
EXPOSE 5000

# 定义环境变量，让 Gunicorn 知道运行在哪个主机和端口
ENV HOST 0.0.0.0
ENV PORT 5000

# 启动 Gunicorn 服务器，运行 Flask 应用
CMD ["gunicorn", "-w", "4", "app:app"]