package events.camel;

import events.camel.services.ItemDeleteVoter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.MulticastDefinition;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DeleteOrNotRouteBuilder extends RouteBuilder {

    public static final String DIRECT_DELETE_OR_NOT = "direct:deleteOrNotDynamic";

    @Autowired
    private AggregationStrategy aggregationStrategy;

    @Autowired
    private List<ItemDeleteVoter> deleteVoters;

    @Override
    public void configure() throws Exception {

        MulticastDefinition routeDefinition = from(DIRECT_DELETE_OR_NOT)
                .multicast(aggregationStrategy).onPrepare(new HeaderEnricher())
                .parallelProcessing()
                .timeout(1000);
        for (ItemDeleteVoter voter : deleteVoters) {
            routeDefinition.bean(new HeaderProducer()).filter(header("tenant").isNotNull()).bean(voter);
        }

    }


}
