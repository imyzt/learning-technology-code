package top.imyzt.learning.rabbitmq.common.utils;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description Constant
 */
public class Constant {

    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_ROUTING = "order.abcd";
    public static final String ORDER_QUEUE = "order-queue";
    public static final String ORDER_CONSUMER_KEY = "order.*";

    public static final String ORDER_SENDING = "0";
    public static final String ORDER_SEND_SUCCESS = "1";
    public static final String ORDER_SEND_FAILURE = "2";
    public static final int ORDER_TIMEOUT_MINUTES = 1;

}
