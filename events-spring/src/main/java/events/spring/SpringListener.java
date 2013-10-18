package events.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import events.common.Event;

public class SpringListener implements ApplicationListener<SpringEvent>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringListener.class);
    Event lastEvent;

    @Override
    public void onApplicationEvent(SpringEvent event)
    {
        LOGGER.info("{} got {}", SpringListener.class.getSimpleName(), event.getEvent());
        lastEvent = event.getEvent();
    }

}
