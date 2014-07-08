package events.camel;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.ProxyHelper;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import events.common.Event;

/**
 * Tests camel event with Java DSL.
 * Used in combination with Spring as Spring is the way to do IoC.
 * Spring only defines beans with Java Config, Camel the routes in Java DSL.
 * @author Anders Malmborg
 *
 */
public class CamelEventJavaTest
{
    private ApplicationContext springCtx;
    private ProducerTemplate producerTemplate;
    private HelloService helloService;

    @BeforeClass
    public void beforeClass() throws Exception
    {
        springCtx = new AnnotationConfigApplicationContext(SpringTestConfig.class);
        CamelContext camelCtx1 = new SpringCamelContext(springCtx);
        camelCtx1.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("direct:event").bean(new PoJoConsumer("1"));
            }
        });
        camelCtx1.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("direct:producer").bean(new HeaderEnricher()).multicast().to("seda:consumer?multipleConsumers=true").to("vm:javaConfigConsumer");
                from("seda:consumer?multipleConsumers=true").bean(new HeaderProducer()).beanRef("consumer1", "handleEvent");
                from("seda:consumer?multipleConsumers=true").bean(new HeaderProducer()).beanRef("consumer2", "handleEvent");
            }
        });
        camelCtx1.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("direct:routingSlip").to("log:events.camel?showHeaders=true").routingSlip(header("routingSlip")).to("log:events.camel?showHeaders=true");
                from("direct:businessPartner").beanRef("businessPartnerConsumer");
                from("direct:vehicle").beanRef("vehicleConsumer");
            }
        });
        helloService = ProxyHelper.createProxy(camelCtx1.getEndpoint("direct:helloProxy"), HelloService.class);
        camelCtx1.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("direct:helloProxy").beanRef("helloService");
            }
        });
        camelCtx1.start();
        producerTemplate = camelCtx1.createProducerTemplate();
        
        //Another CamelContext to demo vm component between contexts.
        CamelContext camelCtx2 = new SpringCamelContext(springCtx);
        camelCtx2.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("vm:javaConfigConsumer").bean(new HeaderProducer()).beanRef("consumer3", "handleEvent");
            }
        });
        camelCtx2.start();
    }
    /**
     * Simple example as in PojoConsumerTest
     */
    @Test
    public void syncEvent()
    {
        assertEquals(producerTemplate.requestBody("direct:event", Event.START_WORKING), Event.START_WORKING);
        assertEquals(producerTemplate.requestBody("direct:event", Event.FINISH_WORKING), Event.FINISH_WORKING);
    }
    
    /**
     * Check if there is no problem as there is nobody on the other side: direct is connected with seda without
     * listeners.
     */
    @Test
    public void fireForgetEvent()
    {
        producerTemplate.sendBody("seda:event",  Event.START_WORKING);
    }
    
    /**
     * Set thread local tenant value, to verify the threading behavior, the tenant is post-fixed by the thread name,
     * i.e. 'main'.
     * Send event, check that both consumers got it and that the tenant was the expected one.
     */
    @Test
    public void publishSubscribe()
    {
        PoJoConsumer consumer1 = springCtx.getBean("consumer1", PoJoConsumer.class);
        PoJoConsumer consumer2 = springCtx.getBean("consumer2", PoJoConsumer.class);
        PoJoConsumer consumer3 = springCtx.getBean("consumer3", PoJoConsumer.class);
        TenantHolder.setTenant("1");
        producerTemplate.setDefaultEndpointUri("direct:producer");
        assertEquals(producerTemplate.requestBody(Event.START_WORKING), Event.START_WORKING);
        assertEquals(consumer1.lastEvent, Event.START_WORKING);
        assertEquals(consumer2.lastEvent, Event.START_WORKING);
        assertEquals(consumer3.lastEvent, Event.START_WORKING);
        assertEquals(producerTemplate.requestBody(Event.FINISH_WORKING), Event.FINISH_WORKING);
        assertEquals(consumer1.lastEvent, Event.FINISH_WORKING);
        assertEquals(consumer2.lastEvent, Event.FINISH_WORKING);
        assertEquals(consumer2.tenant, "1main");
    }
    
    @Test
    public void routing()
    {
        PoJoConsumer businessPartnerConsumer = springCtx.getBean("businessPartnerConsumer", PoJoConsumer.class);
        PoJoConsumer vehicleConsumer = springCtx.getBean("vehicleConsumer", PoJoConsumer.class);
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
        assertEquals(helloService.hi("test"), "hi test");
    }

    private static class SpringTestConfig
    {
        @Bean(name="consumer1")
        public PoJoConsumer poJoConsumer1()
        {
            return new PoJoConsumer("1");
        }
        @Bean(name="consumer2")
        public PoJoConsumer poJoConsumer2()
        {
            return new PoJoConsumer("2");
        }
        @Bean(name="consumer3")
        public PoJoConsumer poJoConsumer3()
        {
            return new PoJoConsumer("3");
        }
        @Bean
        public PoJoConsumer businessPartnerConsumer()
        {
            return new PoJoConsumer("businessPartnerConsumer");
        }
        @Bean
        public PoJoConsumer vehicleConsumer()
        {
            return new PoJoConsumer("vehicleConsumer");
        }
        @Bean
        public HelloService helloService()
        {
            return new HelloServiceImpl();
        }
    }
}
