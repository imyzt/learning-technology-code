import socket

from apscheduler.schedulers.background import BackgroundScheduler
from flask import Flask

from job import schedule
from log import log

app = Flask(__name__)

# 导入路由模块并注册 Blueprint，必须放在主文件，否则gunicorn无法加载到路由
from routes import main_blueprint
app.register_blueprint(main_blueprint)

# 使用端口锁，防止gunicorn多worker启动时，多个进程执行多次问题
try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind(("127.0.0.1", 47200))
except socket.error:
    log.warning("Failed to acquire port lock")
else:
    # job init
    schedule.start()
    log.info("scheduler started")