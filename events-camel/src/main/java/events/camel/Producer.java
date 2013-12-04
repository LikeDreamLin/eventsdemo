package events.camel;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;

/**
 * Wrapper for {@link ProducerTemplate}.
 * Sends an {@link Event}.
 * @author Anders Malmborg
 *
 */
public class Producer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private final ProducerTemplate producerTemplate;

    public Producer(ProducerTemplate producerTemplate)
    {
        this.producerTemplate = producerTemplate;
    }

    public Object sendAndReceiveEvent(Event event)
    {
        LOGGER.info("Sending event '{}' awainting response", event);
        Object response = producerTemplate.requestBody(event);
        LOGGER.info("Event response:'{}'", response.toString());
        return response;
    }
    
    public void sendEvent(Event event)
    {
        LOGGER.info("Sending event {}", event);
        producerTemplate.sendBody(event);
    }

}
