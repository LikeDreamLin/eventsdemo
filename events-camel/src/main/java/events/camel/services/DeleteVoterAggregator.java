package events.camel.services;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class DeleteVoterAggregator implements AggregationStrategy
{

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange)
    {
        if (oldExchange == null)
            return newExchange;
        if (newExchange.getIn().getBody(Boolean.class) == Boolean.FALSE)
            return newExchange;
        return oldExchange;
    }

}
