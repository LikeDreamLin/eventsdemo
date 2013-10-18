package events.camel;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import events.common.Event;

public class CamelConsumerTest
{
    private CamelProducer producer;
    
    @BeforeMethod
    public void setUp()
    {
        producer = new CamelProducer();
    }
    
    @Test
    public void handleEvent()
    {
        producer.sendEvent(Event.START_WORKING);
        assertEquals(CamelConsumer.lastEvent, Event.START_WORKING);
        producer.sendEvent(Event.FINISH_WORKING);
        assertEquals(CamelConsumer.lastEvent, Event.FINISH_WORKING);
    }
}
