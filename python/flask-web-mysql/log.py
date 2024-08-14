# 配置日志记录器
import logging

log = logging.getLogger('my_logger')
log.setLevel(logging.DEBUG)

# 创建一个handler，用于输出到控制台
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.INFO)

# 定义日志格式
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
console_handler.setFormatter(formatter)

# 将handler添加到记录器中
log.addHandler(console_handler)