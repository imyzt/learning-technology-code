from apscheduler.schedulers.background import BackgroundScheduler
from flask import Flask

from job import schedule

app = Flask(__name__)

# 导入路由模块并注册 Blueprint，必须放在主文件，否则gunicorn无法加载到路由
from routes import main_blueprint
app.register_blueprint(main_blueprint)

# job init
schedule.start()