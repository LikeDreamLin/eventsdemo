package events.camel;

import static org.testng.Assert.assertEquals;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import events.common.Event;

public class PojoConsumerTest
{
    private static final String DIRECT = "direct:event";
    private static final String CHANNEL = "seda:inEvents";
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
                from(CHANNEL).bean(PoJoConsumer.class);
                from(DIRECT).to("seda:noListeners?multipleConsumers=true");
            }
        });
        ctx.start();
    }
    
    @Test
    public void syncEvent()
    {
        ProducerTemplate producerTemplate = ctx.createProducerTemplate();
        producerTemplate.setDefaultEndpointUri(CHANNEL);
        assertEquals(producerTemplate.requestBody(Event.START_WORKING), Event.START_WORKING);
        assertEquals(producerTemplate.requestBody(Event.FINISH_WORKING), Event.FINISH_WORKING);
    }
    
    /**
     * Check if there is no problem as there is nobody on the other side: direct is connected with seda without
     * listeners.
     */
    @Test
    public void fireForgetEvent()
    {
        ProducerTemplate producerTemplate = ctx.createProducerTemplate();
        producerTemplate.setDefaultEndpointUri(DIRECT);
        producerTemplate.sendBody(Event.START_WORKING);
    }
}
