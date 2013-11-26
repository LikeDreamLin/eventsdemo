package events.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Insert the current tenant post-fixed by thread name (to demonstrate multi-threading using seda) into the header.
 * @author Anders Malmborg
 *
 */
public class HeaderEnricher implements Processor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderEnricher.class);

    @Override
    public void process(Exchange exchange) throws Exception
    {
        exchange.getIn().setHeader("tenant", TenantHolder.getTenant()+Thread.currentThread().getName());
        LOGGER.info("Extract tenant {}", exchange.getIn().getHeader("tenant", String.class));
    }
}
