package top.imyzt.learning.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Disruptor Use Example
 * @author imyzt
 */
public class UseDemo {

    public static void main(String[] args) {
        Disruptor<MsgEvent> disruptor = new Disruptor<>(MsgEvent::new, 1024, Executors.defaultThreadFactory());

        MessageConsumer mc1 = new MessageConsumer("mc1");
        MessageConsumer mc2 = new MessageConsumer("mc2");
        MessageConsumer mc3 = new MessageConsumer("mc3");

        disruptor.handleEventsWith(mc1, mc2, mc3);
        disruptor.start();

        MessageProducer producer = new MessageProducer(disruptor);
        for (int i = 0; i < 100; i++) {
            producer.send("i==" + i);
        }
    }
}
@RequiredArgsConstructor
class MessageConsumer implements EventHandler<MsgEvent> {
    private final String name;
    @Override
    public void onEvent(MsgEvent msgEvent, long l, boolean b) {
        System.out.printf("[%s]收到消息, 内容===>%s%n", name, msgEvent.getMsg());
    }
}
@RequiredArgsConstructor
class MessageProducer {
    private final Disruptor<MsgEvent> disruptor;
    public void send(String msg) {
        RingBuffer<MsgEvent> ringBuffer = this.disruptor.getRingBuffer();
        long next = ringBuffer.next();
        try {
            MsgEvent event = this.disruptor.get(next);
            event.setMsg(msg);
        } finally {
            ringBuffer.publish(next);
        }
    }
    public void send(List<String> msgs) {
     msgs.forEach(this::send);
    }
}

@Data
class MsgEvent {
    private String msg;
}
