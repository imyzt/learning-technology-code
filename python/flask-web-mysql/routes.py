import json
from datetime import datetime, timedelta

import jwt
from flask import jsonify, Blueprint, request
from werkzeug.security import check_password_hash

from entity.user import User
from login_interceptor import SECRET_KEY, token_required

# 创建一个 Blueprint 对象
main_blueprint = Blueprint('main', __name__)

account_user = {'id': 1, 'account': 'system', 'info': '{}', 'password': 'scrypt:32768:8:1$7OFGV0MtIoUawhpi$346c73de04a359bf5bc99ecd086bb1dc04aca700c9c667b3db944b6b5c9435cbf11be477bcb552c08c1ae9a11ce6e230047fef72a452d989753af57773c2c11c'}

@main_blueprint.route(rule='/list', methods=['GET'])
@token_required
def find_list():
    logs = User.query.order_by(User.created.desc()).all()
    return jsonify([{'id': log.id, 'name': log.name, 'created': log.created.strftime('%Y-%m-%d %H:%M:%S')} for log in logs]), 200

@main_blueprint.route('/login', methods=['POST'])
def login():
    account = request.json.get('account')
    password = request.json.get('password')
    if account_user.get('account') != account:
        return jsonify({'error': 'Wrong account'}), 401
    if check_password_hash(account_user.get('password'), password):
        exp = datetime.utcnow() + timedelta(days=14)
        token = jwt.encode({'id': account_user.get('id'), 'exp': exp}, SECRET_KEY, algorithm='HS256')
        return jsonify({'token': token, 'exp': exp.strftime('%Y-%m-%d %H:%M:%S'), 'info': json.loads(account_user.get('info'))}), 200
    return jsonify({'message': 'Invalid credentials'}), 401

