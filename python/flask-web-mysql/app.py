from apscheduler.schedulers.background import BackgroundScheduler
from flask import Flask
from flask_sqlalchemy import SQLAlchemy

from job import scheduler_insert_user

app = Flask(__name__)


app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:12345678@192.168.64.1/test_db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

# 导入路由模块并注册 Blueprint，必须放在主文件，否则gunicorn无法加载到路由
from routes import main_blueprint
app.register_blueprint(main_blueprint)

schedule = BackgroundScheduler()
schedule.add_job(scheduler_insert_user, 'interval', seconds=10)
# job init
schedule.start()