package events.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;

public class CamelSpringConsumer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CamelSpringConsumer.class);
    Event lastEvent;

    public void handleEvent(Event event)
    {
        LOGGER.info("{} got {}", CamelSpringConsumer.class.getSimpleName(), event);
        lastEvent = event;
    }

}
