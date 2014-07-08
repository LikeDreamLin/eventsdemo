package events.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;

/**
 * Pojo consumer, no camel dependencies.
 * Tracks the last event and the tenant set in {@link TenantHolder} to demonstrate the propagation of some (header) value
 * over threads.
 * @author Anders Malmborg
 *
 */
public class PoJoConsumer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PoJoConsumer.class);
    Event lastEvent;
    String tenant;
    private String id;

    public PoJoConsumer(String id)
    {
        this.id = id;
    }

    public Object handleEvent(Event event)
    {
        LOGGER.info("{}{} got {}", PoJoConsumer.class.getSimpleName(), id, event);
        lastEvent = event;
        tenant = TenantHolder.getTenant();
        return event;
    }
    
    public Event getLastEvent()
    {
        return lastEvent;
    }
}
