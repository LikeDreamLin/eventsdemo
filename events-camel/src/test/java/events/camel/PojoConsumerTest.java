package events.camel;

import static org.testng.Assert.assertEquals;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import events.common.Event;

/**
 * Simplest possible(?) Camel example.
 * @author Anders Malmborg
 *
 */
public class PojoConsumerTest
{
    private static final String DIRECT = "direct:event";
    private CamelContext ctx;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        ctx = new DefaultCamelContext();
        ctx.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from(DIRECT).bean(new PoJoConsumer("1"));
            }
        });
        ctx.start();
    }
    
    @Test
    public void syncEvent()
    {
        ProducerTemplate producerTemplate = ctx.createProducerTemplate();
        producerTemplate.setDefaultEndpointUri(DIRECT);
        assertEquals(producerTemplate.requestBody(Event.START_WORKING), Event.START_WORKING);
        assertEquals(producerTemplate.requestBody(Event.FINISH_WORKING), Event.FINISH_WORKING);
    }
}
