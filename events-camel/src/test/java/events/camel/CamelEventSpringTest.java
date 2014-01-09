package events.camel;

import static org.testng.Assert.*;

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
    @Qualifier("producer.simpleTemplate")
    private ProducerTemplate simpleProducerTemplate;
    
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
     * Simple example as in PojoConsumerTest
     */
    @Test
    public void syncEvent()
    {
        simpleProducerTemplate.setDefaultEndpointUri("direct:event");
        assertEquals(simpleProducerTemplate.requestBody(Event.START_WORKING), Event.START_WORKING);
        assertEquals(simpleProducerTemplate.requestBody(Event.FINISH_WORKING), Event.FINISH_WORKING);
    }
    
    /**
     * Check if there is no problem as there is nobody on the other side: direct is connected with seda without
     * listeners.
     */
    @Test
    public void fireForgetEvent()
    {
        simpleProducerTemplate.setDefaultEndpointUri("direct:event");
        simpleProducerTemplate.sendBody(Event.START_WORKING);
    }
    
    /**
     * Set thread local tenant value, to verify the threading behavior, the tenant is post-fixed by the thread name,
     * i.e. 'main'.
     * Send event, check that both consumers got it and that the tenant was the expected one.
     */
    @Test
    public void publishSubscribe()
    {
        TenantHolder.setTenant("1");
        assertEquals(producer.sendAndReceiveEvent(Event.START_WORKING), Event.START_WORKING);
        assertEquals(consumer1.lastEvent, Event.START_WORKING);
        assertEquals(consumer2.lastEvent, Event.START_WORKING);
        assertEquals(consumer3.lastEvent, Event.START_WORKING);
        assertEquals(producer.sendAndReceiveEvent(Event.FINISH_WORKING), Event.FINISH_WORKING);
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
    
    @Test
    public void proxy()
    {
        HelloService helloService = applicationContext.getBean("helloProxy", HelloService.class);

        assertEquals(helloService.hi("test"), "hi test");
    }

}
