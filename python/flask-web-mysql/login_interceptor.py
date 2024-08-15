from functools import wraps

import jwt
from flask import jsonify, request, g, session
from werkzeug.security import generate_password_hash

SECRET_KEY = "fsdkl8231(*(@NO@!UNLJFLSLA"


def token_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        token = None

        # 从请求的头部获取 Token
        if 'Authorization' in request.headers:
            token = request.headers['Authorization'].split(" ")[1]  # 假设使用的是 Bearer Token

        if not token:
            return jsonify({'message': 'Token is missing'}), 401

        try:
            # 解码 Token
            data = jwt.decode(token, SECRET_KEY, algorithms=['HS256'])
            g.account_id = data['id']
        except:
            return jsonify({'message': 'Invalid or expired token'}), 401

        return f(*args, **kwargs)

    return decorated_function

if __name__ == '__main__':
    print(generate_password_hash('123456'))