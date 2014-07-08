package events.camel.routes;

import org.apache.camel.spring.SpringRouteBuilder;

public class ActiveMqEvents extends SpringRouteBuilder
{

    @Override
    public void configure() throws Exception
    {
        from("activemq:topic:event").to("log:crossng").beanRef("consumer1");
        from("activemq:topic:event").to("log:crossng").beanRef("consumer2");
    }

}
