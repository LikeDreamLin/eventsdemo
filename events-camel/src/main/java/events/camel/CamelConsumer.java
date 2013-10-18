package events.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;

public class CamelConsumer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CamelConsumer.class);
    static Event lastEvent;

    public static void handleEvent(Event event)
    {
        LOGGER.info("{} got {}", CamelConsumer.class.getSimpleName(), event);
        lastEvent = event;
    }

}
