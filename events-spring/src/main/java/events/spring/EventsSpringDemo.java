package events.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import events.common.Event;

public class EventsSpringDemo implements ApplicationContextAware
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsSpringDemo.class);
    private ApplicationContext ctx;
    
    public static void main(String[] args)
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring.xml");
        EventsSpringDemo demo = ctx.getBean(EventsSpringDemo.class);
        demo.sendEvent(Event.FINISH_WORKING);
    }

    public EventsSpringDemo()
    {
    }

    public void sendEvent(Event event)
    {
        LOGGER.info("Sending event {}", event.toString());
        ctx.publishEvent(new SpringEvent(this, event));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        ctx = applicationContext;
    }

}
