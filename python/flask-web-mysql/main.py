from app import app, schedule

if __name__ == '__main__':
    # job init
    # schedule.start()
    #
    # # 导入路由模块并注册 Blueprint
    # from routes import main_blueprint
    # app.register_blueprint(main_blueprint)

    # web init
    app.run(host='0.0.0.0', port=5001, debug=True)