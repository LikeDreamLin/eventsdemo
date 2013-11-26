package events.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts the tenant from the header and set it into thread safe {@link TenantHolder}.
 * @author Anders Malmborg
 *
 */
public class HeaderProducer implements Processor
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderProducer.class);
    @Override
    public void process(Exchange exchange) throws Exception
    {
        LOGGER.info("Set tenant to {}", exchange.getIn().getHeader("tenant", String.class));
        TenantHolder.setTenant(exchange.getIn().getHeader("tenant", String.class));
    }
}
