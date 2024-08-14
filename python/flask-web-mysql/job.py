import random
import string

from app import db, app
from entity.user import User
from log import log


def scheduler_insert_user():
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
