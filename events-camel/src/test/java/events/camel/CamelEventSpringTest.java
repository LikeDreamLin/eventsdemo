package events.camel;

import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import events.common.Event;

@ContextConfiguration(locations="classpath:META-INF/event.camel.spring.xml")
public class CamelEventSpringTest extends AbstractTestNGSpringContextTests
{
    @Autowired 
    private EventProducer producer;
    
    @Autowired
    @Qualifier("consumer1")
    private CamelSpringConsumer consumer1;
    
    @Autowired
    @Qualifier("consumer2")
    private CamelSpringConsumer consumer2;

    @Test
    public void sendEvent()
    {
        producer.sendEvent(Event.START_WORKING);
        assertEquals(consumer1.lastEvent, Event.START_WORKING);
        assertEquals(consumer2.lastEvent, Event.START_WORKING);
        producer.sendEvent(Event.FINISH_WORKING);
        assertEquals(consumer1.lastEvent, Event.FINISH_WORKING);
        assertEquals(consumer2.lastEvent, Event.FINISH_WORKING);
    }

}
