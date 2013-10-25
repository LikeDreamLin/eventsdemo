package events.camel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import events.common.Event;

public class EventsCamelDemoSpring
{
    public static void main(String[] args)
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/event.camel.spring.xml");
        CamelSpringProducer producer = ctx.getBean(CamelSpringProducer.class);
        producer.sendEvent(Event.FINISH_WORKING);
    }

}
