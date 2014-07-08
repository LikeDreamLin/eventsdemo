package events.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Contributor
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Contributor.class);
    private final Integer value;

    public Contributor(Integer value)
    {
        this.value = value;
    }
    
    public Integer contribute(String key)
    {
        LOGGER.info("Contribute {} for key {}", value, key);
        return value;
    }
}
