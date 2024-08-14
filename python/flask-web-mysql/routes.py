from flask import jsonify, Blueprint

from entity.user import User

# 创建一个 Blueprint 对象
main_blueprint = Blueprint('main', __name__)

@main_blueprint.route(rule='/list', methods=['GET'])
def find_list():
    logs = User.query.order_by(User.created.desc()).all()
    return jsonify([{'id': log.id, 'name': log.name, 'created': log.created.strftime('%Y-%m-%d %H:%M:%S')} for log in logs]), 200
