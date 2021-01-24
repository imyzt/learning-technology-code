package top.imyzt.learning.listener.springlistener;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * 测试注解/泛型监听
 * @author imyzt
 * @date 2021/01/24
 */
@SpringBootApplication
public class AnnotationListenerApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(UserEventListener.class);

        context.refresh();

        GenericEvent<User> event = new GenericEvent<>(new User("imyzt"));
        context.publishEvent(event);

        context.publishEvent(new User("imyzt on new User()"));

        context.close();

    }


    public static class UserEventListener implements ApplicationListener<GenericEvent<User>> {

        @Override
        public void onApplicationEvent(GenericEvent<User> event) {
            System.out.println("onApplicationEvent: " + event.getSource().getName());
        }

        @EventListener
        public void onUserEvent(GenericEvent<User> event) {
            System.out.println("onUserEvent: " + event.getSource().getName());
        }
    }

    static class User {

        private String name;

        User(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    static class GenericEvent<T> extends ApplicationEvent {

        GenericEvent(T source) {
            super(source);
        }

        @Override
        @SuppressWarnings("unchecked")
        public T getSource() {
            return (T) super.getSource();
        }
    }


}
