package events.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread safe holder for a tenant value.
 * @author Anders Malmborg
 *
 */
public class TenantHolder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantHolder.class);
    private static final ThreadLocal<String> tenant = new ThreadLocal<String>();

    public static String getTenant()
    {
        LOGGER.info("tenant:{}", tenant.get());
        return tenant.get();
    }
    
    public static void setTenant(String tenantValue)
    {
        tenant.set(tenantValue);
        LOGGER.info("tenant:{}", tenant.get());
    }
    
}
