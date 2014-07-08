package events.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.camel.Contributor;

public class SyncActiveMqContributors extends SpringRouteBuilder
{

    @Override
    public void configure() throws Exception
    {
         from("direct:contribute").to("activemq:topic:contribute")
         .aggregate(new ContributionsAggregator()).header("configkey").completionSize(2).completionTimeout(3000)
         .beanRef("contributionResult", "setContributions");
//        from("direct:contribute").multicast(new ContributionsAggregator()).to("activemq:topic:contribute");
        from("activemq:topic:contribute").bean(new Contributor(1));
        from("activemq:topic:contribute").bean(new Contributor(2));
        // from("activemq:queue:aggregateContributions").aggregate(header("JMSDestination"), new
        // ContributionsAggregator()).completionTimeout(30000).bean(new ContributionResult());

    }

    private static class ContributionsAggregator implements AggregationStrategy
    {
        
        private static final Logger LOGGER =
            LoggerFactory.getLogger(SyncActiveMqContributors.ContributionsAggregator.class);
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange)
        {
            
            Message newIn = newExchange.getIn();
            LOGGER.info("Aggregating {}", newIn.getBody());
            if (oldExchange == null)
            {
                return newExchange;
            }
            Integer oldBody = oldExchange.getIn().getBody(Integer.class);
            Integer newBody = newIn.getBody(Integer.class);
            newIn.setBody(oldBody + newBody);
            return newExchange;
        }
    }
}
