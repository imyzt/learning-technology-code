import random
import string

from apscheduler.schedulers.background import BackgroundScheduler

from log import log


def scheduler_insert_user():
    from entity.user import User
    from app import app
    from db import db
    with app.app_context():
        db.session.add(User(name=generate_random_string(10)))
        db.session.commit()
        log.info("insert user finishing...")



def generate_random_string(length):
    # 定义字符集，包括大小写字母和数字
    characters = string.ascii_letters + string.digits
    # 使用random.choice从字符集中随机选择字符
    random_string = ''.join(random.choice(characters) for _ in range(length))
    return random_string

schedule = BackgroundScheduler()
schedule.add_job(scheduler_insert_user, 'interval', seconds=10)