package events.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;

public class CamelProducer
{
    private static final String CHANNEL = "seda:inEvents";
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsCamelDemo.class);
    private final CamelContext ctx;

    public CamelProducer()
    {
        ctx = new DefaultCamelContext();
        try
        {
            ctx.addRoutes(new RouteBuilder()
            {
                @Override
                public void configure() throws Exception
                {
                    from(CHANNEL).bean(CamelConsumer.class);
                }
            });
        }
        catch (Exception e)
        {
            LOGGER.error("Error configuring route", e);
        }
        try
        {
            ctx.start();
        }
        catch (Exception e)
        {
            LOGGER.error("Error starting camel", e);
        }
    }

    public void sendEvent(Event event)
    {
        ProducerTemplate producerTemplate = ctx.createProducerTemplate();
        LOGGER.info("Sending event {}", event);
        producerTemplate.requestBody(CHANNEL, event);
    }

}
