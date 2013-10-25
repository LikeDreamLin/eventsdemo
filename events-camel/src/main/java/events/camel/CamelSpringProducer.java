package events.camel;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;

public class CamelSpringProducer implements EventProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCamelDemo.class);
    private final ProducerTemplate producerTemplate;

    public CamelSpringProducer(ProducerTemplate producerTemplate)
    {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void sendEvent(Event event)
    {
        LOGGER.info("Sending event {}", event);
        producerTemplate.requestBody(event);
    }

}
