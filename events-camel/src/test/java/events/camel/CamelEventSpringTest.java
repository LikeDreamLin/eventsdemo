package events.camel;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import events.common.Event;

/**
 * Tests camel wiring.
 * @author Anders Malmborg
 *
 */
@ContextConfiguration(locations="classpath:META-INF/event.camel.spring.xml")
public class CamelEventSpringTest extends AbstractTestNGSpringContextTests
{
    @Autowired 
    private Producer producer;
    
    @Autowired
    @Qualifier("producer.template")
    private ProducerTemplate producerTemplate;
    
    @Autowired
    @Qualifier("consumer1")
    private PoJoConsumer consumer1;
    
    @Autowired
    @Qualifier("consumer2")
    private PoJoConsumer consumer2;
    
    @Autowired
    @Qualifier("consumer3")
    private PoJoConsumer consumer3;

    @Autowired
    @Qualifier("businessPartnerConsumer")
    private PoJoConsumer businessPartnerConsumer;
    
    @Autowired
    @Qualifier("vehicleConsumer")
    private PoJoConsumer vehicleConsumer;

    /**
     * Set thread local tenant value, to verify the threading behavior, the tenant is post-fixed by the thread name,
     * i.e. 'main'.
     * Send event, check that both consumers got it and that the tenant was the expected one.
     */
    @Test
    public void publishSubscribe()
    {
        TenantHolder.setTenant("1");
        producer.sendEvent(Event.START_WORKING);
        assertEquals(consumer1.lastEvent, Event.START_WORKING);
        assertEquals(consumer2.lastEvent, Event.START_WORKING);
        assertEquals(consumer3.lastEvent, Event.START_WORKING);
        producer.sendEvent(Event.FINISH_WORKING);
        assertEquals(consumer1.lastEvent, Event.FINISH_WORKING);
        assertEquals(consumer2.lastEvent, Event.FINISH_WORKING);
        assertEquals(consumer2.tenant, "1main");
    }
    
    @Test
    public void routing()
    {
        producerTemplate.sendBodyAndHeader("direct:routingSlip", Event.START_WORKING, "routingSlip", "direct:businessPartner");
        assertEquals(businessPartnerConsumer.lastEvent, Event.START_WORKING);
        assertNull(vehicleConsumer.lastEvent);
        producerTemplate.sendBodyAndHeader("direct:routingSlip", Event.FINISH_WORKING, "routingSlip", "direct:vehicle");
        assertEquals(businessPartnerConsumer.lastEvent, Event.START_WORKING);
        assertEquals(vehicleConsumer.lastEvent, Event.FINISH_WORKING);
    }

}
